package com.coursecart.enrollment.controller;

import com.coursecart.enrollment.dto.EnrollmentCreateRequest;
import com.coursecart.enrollment.dto.EnrollmentDTO;
import com.coursecart.enrollment.dto.LessonCompleteRequest;
import com.coursecart.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByUserId(userId));
    }

    @GetMapping("/{enrollmentId}/progress")
    public ResponseEntity<List<Long>> getCompletedLessonIds(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.getCompletedLessonIds(enrollmentId));
    }

    @PostMapping("/{enrollmentId}/lessons/{lessonId}/complete")
    public ResponseEntity<Void> markLessonComplete(@PathVariable Long enrollmentId, @PathVariable Long lessonId, @Valid @RequestBody LessonCompleteRequest request) {
        enrollmentService.markLessonComplete(enrollmentId, lessonId, request.getCompleted());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countTotalEnrollments() {
        return ResponseEntity.ok(enrollmentService.countTotalEnrollments());
    }

    @GetMapping("/internal/check")
    public ResponseEntity<Boolean> checkEnrollment(@RequestParam Long userId, @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.checkEnrollment(userId, courseId));
    }

    @PostMapping("/internal/create")
    public ResponseEntity<EnrollmentDTO> createEnrollment(@Valid @RequestBody EnrollmentCreateRequest request) {
        return new ResponseEntity<>(enrollmentService.createEnrollment(request.getUserId(), request.getCourseId()), HttpStatus.CREATED);
    }
}
