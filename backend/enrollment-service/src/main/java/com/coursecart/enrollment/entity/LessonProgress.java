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
@Table(name = "lesson_progress")
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private Timestamp completedAt;
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        if (Boolean.TRUE.equals(isCompleted)) {
            this.completedAt = Timestamp.from(Instant.now());
        } else {
            this.completedAt = null;
        }
    }
}
