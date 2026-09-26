package com.coursecart.catalog.service;

import com.coursecart.catalog.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatalogService {
    // Categories
    List<CategoryDTO> getAllCategories();
    CategoryDTO createCategory(CategoryRequest request);
    CategoryDTO updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);

    // Courses (Public)
    Page<CourseDTO> getActiveCourses(Pageable pageable, Long categoryId, String title);
    CourseDetailDTO getActiveCourseById(Long id);

    // Courses (Admin)
    List<CourseDTO> getAllCoursesAdmin();
    CourseDTO createCourse(CourseRequest request);
    CourseDTO updateCourse(Long id, CourseRequest request);
    CourseDTO updateCourseStatus(Long id, CourseStatusRequest request);
    void deleteCourse(Long id);
    long countActiveCourses();

    // Lessons
    List<LessonDTO> getLessonsForCourse(Long courseId);
    LessonDTO createLesson(Long courseId, LessonRequest request);
    LessonDTO updateLesson(Long courseId, Long lessonId, LessonRequest request);
    void deleteLesson(Long courseId, Long lessonId);
}

