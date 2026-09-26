package com.coursecart.catalog.repository;

import com.coursecart.catalog.entity.Course;
import com.coursecart.catalog.entity.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    Page<Course> findByStatusAndCategoryId(CourseStatus status, Long categoryId, Pageable pageable);
    Page<Course> findByStatusAndTitleContainingIgnoreCase(CourseStatus status, String title, Pageable pageable);
    Page<Course> findByStatusAndCategoryIdAndTitleContainingIgnoreCase(CourseStatus status, Long categoryId, String title, Pageable pageable);
    List<Course> findByCategoryId(Long categoryId);
    long countByStatus(CourseStatus status);
}

