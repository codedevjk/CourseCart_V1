package com.coursecart.catalog.service;

import com.coursecart.catalog.dto.*;
import com.coursecart.catalog.entity.Category;
import com.coursecart.catalog.entity.Course;
import com.coursecart.catalog.entity.CourseStatus;
import com.coursecart.catalog.entity.Lesson;
import com.coursecart.catalog.exception.CatalogServiceException;
import com.coursecart.catalog.repository.CategoryRepository;
import com.coursecart.catalog.repository.CourseRepository;
import com.coursecart.catalog.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CatalogServiceImpl catalogService;

    private Category category;
    private Course course;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Programming");

        course = new Course();
        course.setId(1L);
        course.setCategory(category);
        course.setTitle("Java 101");
        course.setDescription("Learn Java");
        course.setPrice(new BigDecimal("19.99"));
        course.setStatus(CourseStatus.ACTIVE);
        course.setBestseller(false);
        course.setRatingCount(0);
        course.setLessonCount(0);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setCourse(course);
        lesson.setTitle("Intro to Java");
        lesson.setContent("System.out.println");
        lesson.setDisplayOrder(1);
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Programming");

        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Programming");
        when(modelMapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(dto);

        CategoryDTO response = catalogService.createCategory(request);

        assertNotNull(response);
        assertEquals("Programming", response.getName());
    }

    @Test
    void testGetActiveCourses() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course));
        when(courseRepository.findByStatus(CourseStatus.ACTIVE, pageRequest)).thenReturn(coursePage);
        
        CourseDTO dto = new CourseDTO();
        dto.setTitle("Java 101");
        when(modelMapper.map(any(Course.class), eq(CourseDTO.class))).thenReturn(dto);

        Page<CourseDTO> response = catalogService.getActiveCourses(pageRequest, null, null);

        assertEquals(1, response.getTotalElements());
        assertEquals("Java 101", response.getContent().get(0).getTitle());
    }

    @Test
    void testGetActiveCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.singletonList(lesson));

        CourseDetailDTO dto = new CourseDetailDTO();
        dto.setTitle("Java 101");
        when(modelMapper.map(any(Course.class), eq(CourseDetailDTO.class))).thenReturn(dto);

        CourseDetailDTO response = catalogService.getActiveCourseById(1L);

        assertNotNull(response);
        assertEquals("Java 101", response.getTitle());
    }

    @Test
    void testCreateCourse() {
        CourseRequest request = new CourseRequest();
        request.setCategoryId(1L);
        request.setTitle("Java 101");
        request.setDescription("Learn Java");
        request.setPrice(new BigDecimal("19.99"));
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        
        CourseDTO dto = new CourseDTO();
        dto.setTitle("Java 101");
        when(modelMapper.map(any(Course.class), eq(CourseDTO.class))).thenReturn(dto);

        CourseDTO response = catalogService.createCourse(request);

        assertNotNull(response);
        assertEquals("Java 101", response.getTitle());
    }

    @Test
    void testUpdateCourseStatus() {
        CourseStatusRequest request = new CourseStatusRequest();
        request.setStatus(CourseStatus.INACTIVE);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setStatus(CourseStatus.INACTIVE);
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        
        CourseDTO dto = new CourseDTO();
        dto.setStatus(CourseStatus.INACTIVE);
        when(modelMapper.map(any(Course.class), eq(CourseDTO.class))).thenReturn(dto);

        CourseDTO response = catalogService.updateCourseStatus(1L, request);

        assertEquals(CourseStatus.INACTIVE, response.getStatus());
    }

    // --- deleteCourse tests ---

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        assertDoesNotThrow(() -> catalogService.deleteCourse(1L));

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testDeleteLesson_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).delete(lesson);

        assertDoesNotThrow(() -> catalogService.deleteLesson(1L, 1L));

        verify(lessonRepository, times(1)).delete(lesson);
    }
}
