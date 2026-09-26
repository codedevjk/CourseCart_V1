package com.coursecart.enrollment.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private Timestamp enrolledAt;
    
    @PrePersist
    protected void onCreate() {
        if (enrolledAt == null) {
            enrolledAt = Timestamp.from(Instant.now());
        }
    }
}
