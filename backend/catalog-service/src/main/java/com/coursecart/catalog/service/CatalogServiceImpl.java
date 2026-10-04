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
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new CatalogServiceException(HttpStatus.CONFLICT, ErrorMessages.CATEGORY_NAME_EXISTS);
        }
        Category category = new Category();
        category.setName(request.getName());
        Category saved = categoryRepository.save(category);
        return mapToCategoryDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.CATEGORY_NOT_FOUND));

        if (!category.getName().equals(request.getName()) && categoryRepository.findByName(request.getName()).isPresent()) {
            throw new CatalogServiceException(HttpStatus.CONFLICT, ErrorMessages.CATEGORY_NAME_EXISTS);
        }

        category.setName(request.getName());
        Category updated = categoryRepository.save(category);
        return mapToCategoryDTO(updated);
    }

    @Override
    @Transactional
        public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.CATEGORY_NOT_FOUND));
        List<Course> attachedCourses = courseRepository.findByCategoryId(id);
        
        boolean hasActiveCourses = attachedCourses.stream()
                .anyMatch(course -> course.getStatus() == CourseStatus.ACTIVE);
                
        if (hasActiveCourses) {
            throw new CatalogServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.CATEGORY_HAS_ACTIVE_COURSES);
        }
        categoryRepository.delete(category);
    }

    // --- COURSES PUBLIC ---

    @Override
    public Page<CourseDTO> getActiveCourses(Pageable pageable, Long categoryId, String title) {
        Page<Course> courses;
        if (categoryId != null && title != null && !title.isEmpty()) {
            courses = courseRepository.findByStatusAndCategoryIdAndTitleContainingIgnoreCase(CourseStatus.ACTIVE, categoryId, title, pageable);
        } else if (categoryId != null) {
            courses = courseRepository.findByStatusAndCategoryId(CourseStatus.ACTIVE, categoryId, pageable);
        } else if (title != null && !title.isEmpty()) {
            courses = courseRepository.findByStatusAndTitleContainingIgnoreCase(CourseStatus.ACTIVE, title, pageable);
        } else {
            courses = courseRepository.findByStatus(CourseStatus.ACTIVE, pageable);
        }
        return courses.map(this::mapToCourseDTO);
    }

    @Override
    public CourseDetailDTO getActiveCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));

        return mapToCourseDetailDTO(course);
    }

    // --- COURSES ADMIN ---

    @Override
    public List<CourseDTO> getAllCoursesAdmin() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.CATEGORY_NOT_FOUND));

        Course course = new Course();
        course.setCategory(category);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setStatus(CourseStatus.DRAFT);
        course.setOriginalPrice(request.getOriginalPrice());
        course.setInstructorName(request.getInstructorName());
        course.setRatingCount(0);
        course.setBestseller(false);
        course.setLessonCount(0);
        Course saved = courseRepository.save(course);
        return mapToCourseDTO(saved);
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.CATEGORY_NOT_FOUND));

        course.setCategory(category);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setOriginalPrice(request.getOriginalPrice());
        course.setInstructorName(request.getInstructorName());

        Course updated = courseRepository.save(course);
        return mapToCourseDTO(updated);
    }

    @Override
    @Transactional
    public CourseDTO updateCourseStatus(Long id, CourseStatusRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));
                
        if (request.getStatus() == CourseStatus.ACTIVE && course.getCategory() == null) {
            throw new CatalogServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.CATEGORY_REQUIRED_FOR_ACTIVATION);
        }
        
        course.setStatus(request.getStatus());
        Course updated = courseRepository.save(course);
        return mapToCourseDTO(updated);
    }

    @Override
    @Transactional
        public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CatalogServiceException(HttpStatus.NOT_FOUND, ErrorMessages.COURSE_NOT_FOUND));
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByDisplayOrderAsc(id);
        for (Lesson l : lessons) {
            lessonRepository.delete(l);
        }
        courseRepository.delete(course);
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







