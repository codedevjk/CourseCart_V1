package com.coursecart.enrollment.service;

import com.coursecart.enrollment.dto.EnrollmentDTO;
import com.coursecart.enrollment.entity.Enrollment;
import com.coursecart.enrollment.entity.LessonProgress;
import com.coursecart.enrollment.exception.ResourceNotFoundException;
import com.coursecart.enrollment.repository.EnrollmentRepository;
import com.coursecart.enrollment.repository.LessonProgressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final ModelMapper modelMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, LessonProgressRepository lessonProgressRepository, ModelMapper modelMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.lessonProgressRepository = lessonProgressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByUserId(Long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCompletedLessonIds(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new ResourceNotFoundException("Enrollment not found");
        }

        return lessonProgressRepository.findByEnrollmentIdAndIsCompletedTrue(enrollmentId).stream()
                .map(LessonProgress::getLessonId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markLessonComplete(Long enrollmentId, Long lessonId, boolean completed) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        Optional<LessonProgress> existingProgress = lessonProgressRepository.findByEnrollmentIdAndLessonId(enrollmentId, lessonId);

        if (existingProgress.isPresent()) {
            LessonProgress progress = existingProgress.get();
            progress.setIsCompleted(completed);
            lessonProgressRepository.save(progress);
        } else {
            if (completed) {
                LessonProgress progress = new LessonProgress();
                progress.setEnrollment(enrollment);
                progress.setLessonId(lessonId);
                progress.setIsCompleted(true);
                lessonProgressRepository.save(progress);
            }
        }
    }

    @Override
    public Map<String, Long> countTotalEnrollments() {
        return Map.of("totalEnrollments", enrollmentRepository.count());
    }

    @Override
    public boolean checkEnrollment(Long userId, Long courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId).isPresent();
    }

    @Override
    @Transactional
    public EnrollmentDTO createEnrollment(Long userId, Long courseId) {
        if (checkEnrollment(userId, courseId)) {
            throw new IllegalArgumentException("User is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        
        Enrollment saved = enrollmentRepository.save(enrollment);
        return mapToDTO(saved);
    }

    private EnrollmentDTO mapToDTO(Enrollment enrollment) {
        return modelMapper.map(enrollment, EnrollmentDTO.class);
    }
}
