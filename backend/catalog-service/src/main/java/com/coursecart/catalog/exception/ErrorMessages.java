package com.coursecart.catalog.exception;

public final class ErrorMessages {
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String COURSE_NOT_FOUND = "Course not found";
    public static final String LESSON_NOT_FOUND = "Lesson not found";
    public static final String CATEGORY_HAS_ACTIVE_COURSES = "Cannot delete category because it has active courses attached";
    public static final String CATEGORY_NAME_EXISTS = "Category name already exists";
    public static final String CATEGORY_REQUIRED_FOR_ACTIVATION = "Cannot activate a course without a category";

    private ErrorMessages() {}
}
