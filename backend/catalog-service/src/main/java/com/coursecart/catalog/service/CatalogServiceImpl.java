package com.coursecart.catalog.service;

import com.coursecart.catalog.dto.*;
import com.coursecart.catalog.entity.Category;
import com.coursecart.catalog.entity.Course;
import com.coursecart.catalog.entity.CourseStatus;
import com.coursecart.catalog.entity.Lesson;
import com.coursecart.catalog.exception.ResourceNotFoundException;
import com.coursecart.catalog.repository.CategoryRepository;
import com.coursecart.catalog.repository.CourseRepository;
import com.coursecart.catalog.repository.LessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    public CatalogServiceImpl(CategoryRepository categoryRepository, CourseRepository courseRepository, LessonRepository lessonRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.modelMapper = modelMapper;
    }

    // --- CATEGORIES ---

    @Override
    public List<CategoryDTO> getAllCategories() {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Fetch all categories from repository and map to DTOs (US 04).");
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Validate category name uniqueness, save entity, and return DTO (US 04).");
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Find category, validate new name uniqueness, update, and save (US 04).");
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Delete category, ensuring no active courses are attached (US 04).");
    }

    // --- COURSES PUBLIC ---

    @Override
    public Page<CourseDTO> getActiveCourses(Pageable pageable, Long categoryId, String title) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Fetch ACTIVE courses using pagination, applying dynamic category or title filters (US 08).");
    }

    @Override
    public CourseDetailDTO getActiveCourseById(Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Fetch course by id and return detailed DTO (US 09).");
    }

    // --- COURSES ADMIN ---

    @Override
    public List<CourseDTO> getAllCoursesAdmin() {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Fetch all courses regardless of status for admin (US 05).");
    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Create new course with DRAFT status and associate with category (US 05).");
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Update existing course metadata (US 05).");
    }

    @Override
    @Transactional
    public CourseDTO updateCourseStatus(Long id, CourseStatusRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Update course status. Ensure it has a category before setting ACTIVE (US 07).");
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Cascade delete course and its lessons (US 05).");
    }

    @Override
    public long countActiveCourses() {
        return courseRepository.countByStatus(CourseStatus.ACTIVE);
    }

    // --- LESSONS ---

    @Override
    public List<LessonDTO> getLessonsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return lessonRepository.findByCourseIdOrderByDisplayOrderAsc(course.getId()).stream()
                .map(this::mapToLessonDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LessonDTO createLesson(Long courseId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setDisplayOrder(request.getDisplayOrder());
        Lesson saved = lessonRepository.save(lesson);
        return mapToLessonDTO(saved);
    }

    @Override
    @Transactional
    public LessonDTO updateLesson(Long courseId, Long lessonId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourse().getId().equals(course.getId())) {
            throw new IllegalArgumentException("Lesson does not belong to the specified course");
        }

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setDisplayOrder(request.getDisplayOrder());

        Lesson updated = lessonRepository.save(lesson);
        return mapToLessonDTO(updated);
    }

    @Override
    @Transactional
    public void deleteLesson(Long courseId, Long lessonId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourse().getId().equals(course.getId())) {
            throw new IllegalArgumentException("Lesson does not belong to the specified course");
        }

        lessonRepository.delete(lesson);
    }

    // --- MAPPERS ---

    private CategoryDTO mapToCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        return modelMapper.map(category, CategoryDTO.class);
    }

    private CourseDTO mapToCourseDTO(Course course) {
        return modelMapper.map(course, CourseDTO.class);
    }

    private CourseDetailDTO mapToCourseDetailDTO(Course course) {
        CourseDetailDTO dto = modelMapper.map(course, CourseDetailDTO.class);
        List<LessonDTO> lessons = lessonRepository.findByCourseIdOrderByDisplayOrderAsc(course.getId())
                .stream()
                .map(this::mapToLessonDTO)
                .collect(Collectors.toList());
        dto.setLessons(lessons);
        return dto;
    }

    private LessonDTO mapToLessonDTO(Lesson lesson) {
        return modelMapper.map(lesson, LessonDTO.class);
    }
}







