package com.coursecart.catalog.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "instructor_name")
    private String instructorName;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "is_bestseller")
    private Boolean bestseller = false;

    @org.hibernate.annotations.Formula("(SELECT count(*) FROM lessons l WHERE l.course_id = id)")
    private Integer lessonCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;
}
