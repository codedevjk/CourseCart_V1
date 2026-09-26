package com.coursecart.enrollment.service;

import com.coursecart.enrollment.dto.EnrollmentDTO;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    List<EnrollmentDTO> getEnrollmentsByUserId(Long userId);
    List<Long> getCompletedLessonIds(Long enrollmentId);
    void markLessonComplete(Long enrollmentId, Long lessonId, boolean completed);
    Map<String, Long> countTotalEnrollments();
    boolean checkEnrollment(Long userId, Long courseId);
    EnrollmentDTO createEnrollment(Long userId, Long courseId);
}
