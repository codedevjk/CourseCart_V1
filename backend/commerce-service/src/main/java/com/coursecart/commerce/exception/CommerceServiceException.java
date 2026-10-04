package com.coursecart.commerce.exception;

import org.springframework.http.HttpStatus;

public class CommerceServiceException extends RuntimeException {

    private final HttpStatus status;

    public CommerceServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
