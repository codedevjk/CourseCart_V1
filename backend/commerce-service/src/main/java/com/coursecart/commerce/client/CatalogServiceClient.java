package com.coursecart.commerce.client;

import com.coursecart.commerce.dto.CourseDTO;
import com.coursecart.commerce.exception.ResourceNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CatalogServiceClient {

    private static final String CATALOG_SERVICE_URL = "http://catalog-service";

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public CatalogServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Fetches course details from the Catalog Service using WebClient.
     * Circuit breaker opens after 50% failure rate in a 10-call sliding window.
     * Falls back gracefully when Catalog Service is unavailable.
     */
    @CircuitBreaker(name = "catalogService", fallbackMethod = "getCourseFallback")
    public CourseDTO getCourseById(Long courseId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(CATALOG_SERVICE_URL + "/api/catalog/courses/" + courseId)
                    .retrieve()
                    .bodyToMono(CourseDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
    }

    public CourseDTO getCourseFallback(Long courseId, Throwable t) {
        if (t instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) t;
        }
        throw new RuntimeException("Catalog service is currently unavailable. Please try again later.");
    }
}