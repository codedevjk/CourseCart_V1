package com.coursecart.catalog.exception;

import org.springframework.http.HttpStatus;

public class CatalogServiceException extends RuntimeException {

    private final HttpStatus status;

    public CatalogServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
