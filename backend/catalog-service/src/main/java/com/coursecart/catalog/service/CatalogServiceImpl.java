package com.coursecart.catalog.service;

import com.coursecart.catalog.dto.*;
import com.coursecart.catalog.entity.Category;
import com.coursecart.catalog.entity.Course;
import com.coursecart.catalog.entity.CourseStatus;
import com.coursecart.catalog.entity.Lesson;
import com.coursecart.catalog.exception.CatalogServiceException;
import com.coursecart.catalog.exception.ErrorMessages;
import com.coursecart.catalog.repository.CategoryRepository;
import com.coursecart.catalog.repository.CourseRepository;
import com.coursecart.catalog.repository.LessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement getAllCategories");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public CategoryDTO createCategory(CategoryRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement createCategory");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public CategoryDTO updateCategory(Long id, CategoryRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement updateCategory");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public void deleteCategory(Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement deleteCategory");
    }

    // --- COURSES PUBLIC ---

    @Override
    public Page<CourseDTO> getActiveCourses(Pageable pageable, Long categoryId, String title) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement getActiveCourses");
    }

    @Override
    public CourseDetailDTO getActiveCourseById(Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement getActiveCourseById");
    }

    // --- COURSES ADMIN ---

    @Override
    public List<CourseDTO> getAllCoursesAdmin() {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement getAllCoursesAdmin");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public CourseDTO createCourse(CourseRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement createCourse");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public CourseDTO updateCourse(Long id, CourseRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement updateCourse");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public CourseDTO updateCourseStatus(Long id, CourseStatusRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement updateCourseStatus");
    }

    @Override
    // TODO[TRAINEE]: Add appropriate transaction annotation
    public void deleteCourse(Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement deleteCourse");
    }

    @Override
    public long countActiveCourses() {
        return courseRepository.countByStatus(CourseStatus.ACTIVE);
    }

    // --- LESSONS ---

    @Override
    public List<LessonDTO> getLessonsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));
        return lessonRepository.findByCourseIdOrderByDisplayOrderAsc(course.getId()).stream()
                .map(this::mapToLessonDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LessonDTO createLesson(Long courseId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));

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
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.LESSON_NOT_FOUND));

        if (!lesson.getCourse().getId().equals(course.getId())) {
            throw new CatalogServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.LESSON_NOT_FOUND);
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
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.LESSON_NOT_FOUND));

        if (!lesson.getCourse().getId().equals(course.getId())) {
            throw new CatalogServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.LESSON_NOT_FOUND);
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







