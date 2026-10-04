package com.coursecart.enrollment.exception;

import org.springframework.http.HttpStatus;

public class EnrollmentServiceException extends RuntimeException {

    private final HttpStatus status;

    public EnrollmentServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
