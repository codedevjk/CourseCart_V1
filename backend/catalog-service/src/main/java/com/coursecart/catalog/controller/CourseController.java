package com.coursecart.catalog.controller;

import com.coursecart.catalog.dto.*;
import com.coursecart.catalog.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog/courses")
public class CourseController {

    private final CatalogService catalogService;

    public CourseController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // --- PUBLIC COURSE APIs ---

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> getActiveCourses(
            Pageable pageable,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(catalogService.getActiveCourses(pageable, categoryId, title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getActiveCourseById(id));
    }

    // --- ADMIN COURSE APIs ---

    @GetMapping("/admin")
    public ResponseEntity<List<CourseDTO>> getAllCoursesAdmin() {
        return ResponseEntity.ok(catalogService.getAllCoursesAdmin());
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseRequest request) {
        return new ResponseEntity<>(catalogService.createCourse(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(catalogService.updateCourse(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CourseDTO> updateCourseStatus(@PathVariable Long id, @Valid @RequestBody CourseStatusRequest request) {
        return ResponseEntity.ok(catalogService.updateCourseStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        catalogService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countCourses() {
        return ResponseEntity.ok(Map.of("totalCourses", catalogService.countActiveCourses()));
    }

    // --- LESSON APIs ---

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<LessonDTO>> getLessons(@PathVariable Long courseId) {
        return ResponseEntity.ok(catalogService.getLessonsForCourse(courseId));
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<LessonDTO> createLesson(@PathVariable Long courseId, @Valid @RequestBody LessonRequest request) {
        return new ResponseEntity<>(catalogService.createLesson(courseId, request), HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long courseId, @PathVariable Long lessonId, @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(catalogService.updateLesson(courseId, lessonId, request));
    }

    @DeleteMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        catalogService.deleteLesson(courseId, lessonId);
        return ResponseEntity.noContent().build();
    }
}

