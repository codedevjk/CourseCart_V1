package com.coursecart.enrollment.service;

import com.coursecart.enrollment.dto.EnrollmentDTO;
import com.coursecart.enrollment.entity.Enrollment;
import com.coursecart.enrollment.entity.LessonProgress;
import com.coursecart.enrollment.exception.EnrollmentServiceException;
import com.coursecart.enrollment.repository.EnrollmentRepository;
import com.coursecart.enrollment.repository.LessonProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private LessonProgressRepository lessonProgressRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Enrollment enrollment;
    private LessonProgress lessonProgress;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment();
        enrollment.setId(100L);
        enrollment.setUserId(1L);
        enrollment.setCourseId(5L);

        lessonProgress = new LessonProgress();
        lessonProgress.setEnrollment(enrollment);
        lessonProgress.setLessonId(101L);
        lessonProgress.setId(200L);
        lessonProgress.setIsCompleted(true);
    }

    @Test
    void testGetEnrollmentsByUserId() {
        when(enrollmentRepository.findByUserId(1L)).thenReturn(Collections.singletonList(enrollment));
        
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setCourseId(5L);
        when(modelMapper.map(any(Enrollment.class), eq(EnrollmentDTO.class))).thenReturn(dto);

        List<EnrollmentDTO> result = enrollmentService.getEnrollmentsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getCourseId());
    }

    @Test
    void testGetCompletedLessonIds() {
        when(enrollmentRepository.existsById(100L)).thenReturn(true);
        when(lessonProgressRepository.findByEnrollmentIdAndIsCompletedTrue(100L)).thenReturn(Collections.singletonList(lessonProgress));

        List<Long> result = enrollmentService.getCompletedLessonIds(100L);

        assertEquals(1, result.size());
        assertEquals(101L, result.get(0));
    }

    @Test
    void testGetCompletedLessonIds_EnrollmentNotFound_ThrowsResourceNotFoundException() {
        when(enrollmentRepository.existsById(999L)).thenReturn(false);

        assertThrows(EnrollmentServiceException.class, () -> enrollmentService.getCompletedLessonIds(999L));
    }

    @Test
    void testCreateEnrollment_Success() {
        when(enrollmentRepository.findByUserIdAndCourseId(1L, 5L)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(100L);
        when(modelMapper.map(any(Enrollment.class), eq(EnrollmentDTO.class))).thenReturn(dto);

        EnrollmentDTO result = enrollmentService.createEnrollment(1L, 5L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    void testCreateEnrollment_Duplicate_ThrowsException() {
        when(enrollmentRepository.findByUserIdAndCourseId(1L, 5L)).thenReturn(Optional.of(enrollment));

        assertThrows(EnrollmentServiceException.class, () -> enrollmentService.createEnrollment(1L, 5L));

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void testMarkLessonComplete_NewProgress() {
        when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentIdAndLessonId(100L, 101L)).thenReturn(Optional.empty());
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenReturn(lessonProgress);

        enrollmentService.markLessonComplete(100L, 101L, true);

        verify(lessonProgressRepository, times(1)).save(any(LessonProgress.class));
    }

    @Test
    void testMarkLessonComplete_NoExistingRow_CompletedFalse_NoInsert() {
        when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentIdAndLessonId(100L, 101L)).thenReturn(Optional.empty());

        enrollmentService.markLessonComplete(100L, 101L, false);

        verify(lessonProgressRepository, never()).save(any());
    }

    @Test
    void testMarkLessonComplete_ExistingProgress() {
        when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentIdAndLessonId(100L, 101L)).thenReturn(Optional.of(lessonProgress));

        enrollmentService.markLessonComplete(100L, 101L, false);

        verify(lessonProgressRepository, times(1)).save(any(LessonProgress.class));
        assertFalse(lessonProgress.getIsCompleted());
    }

    @Test
    void testMarkLessonComplete_RepeatedComplete_IdempotentUpdateOfExistingRow() {
        lessonProgress.setIsCompleted(true);
        when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentIdAndLessonId(100L, 101L)).thenReturn(Optional.of(lessonProgress));

        enrollmentService.markLessonComplete(100L, 101L, true);

        assertTrue(lessonProgress.getIsCompleted());
        verify(lessonProgressRepository, times(1)).save(lessonProgress);
    }

    @Test
    void testMarkLessonComplete_EnrollmentNotFound_ThrowsResourceNotFoundException() {
        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EnrollmentServiceException.class,
                () -> enrollmentService.markLessonComplete(999L, 101L, true));
    }

    @Test
    void testCountTotalEnrollments_ReturnsTotalEnrollmentsKey() {
        when(enrollmentRepository.count()).thenReturn(37L);

        Map<String, Long> result = enrollmentService.countTotalEnrollments();

        assertNotNull(result);
        assertTrue(result.containsKey("totalEnrollments"));
        assertEquals(37L, result.get("totalEnrollments"));
    }

    @Test
    void testCheckEnrollment_Enrolled_ReturnsTrue() {
        when(enrollmentRepository.findByUserIdAndCourseId(1L, 5L)).thenReturn(Optional.of(enrollment));

        assertTrue(enrollmentService.checkEnrollment(1L, 5L));
    }

    @Test
    void testCheckEnrollment_NotEnrolled_ReturnsFalse() {
        when(enrollmentRepository.findByUserIdAndCourseId(1L, 99L)).thenReturn(Optional.empty());

        assertFalse(enrollmentService.checkEnrollment(1L, 99L));
    }
}
