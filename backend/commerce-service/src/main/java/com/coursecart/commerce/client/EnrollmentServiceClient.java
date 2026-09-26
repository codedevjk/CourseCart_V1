package com.coursecart.commerce.client;

import com.coursecart.commerce.dto.EnrollmentCreateRequest;
import com.coursecart.commerce.dto.EnrollmentDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class EnrollmentServiceClient {

    private static final String ENROLLMENT_SERVICE_URL = "http://enrollment-service";

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public EnrollmentServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Checks whether a user is already enrolled in a course via the Enrollment Service.
     * Circuit breaker opens after 50% failure rate in a 10-call sliding window.
     */
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "isUserEnrolledFallback")
    public boolean checkEnrollment(Long userId, Long courseId) {
        try {
            Boolean isEnrolled = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("enrollment-service")
                            .path("/api/enrollments/internal/check")
                            .queryParam("userId", userId)
                            .queryParam("courseId", courseId)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return isEnrolled != null && isEnrolled;
        } catch (WebClientResponseException e) {
            return false;
        }
    }

    /**
     * Creates a new enrollment in the Enrollment Service after a successful order.
     * Circuit breaker opens after 50% failure rate in a 10-call sliding window.
     */
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "createEnrollmentFallback")
    public EnrollmentDTO createEnrollment(EnrollmentCreateRequest request) {
        return webClientBuilder.build()
                .post()
                .uri(ENROLLMENT_SERVICE_URL + "/api/enrollments/internal/create")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EnrollmentDTO.class)
                .block();
    }

    public boolean isUserEnrolledFallback(Long userId, Long courseId, Throwable t) {
        throw new RuntimeException("Enrollment service is currently unavailable. Please try again later.");
    }

    public EnrollmentDTO createEnrollmentFallback(EnrollmentCreateRequest request, Throwable t) {
        throw new RuntimeException("Failed to process enrollment. Enrollment service unavailable. Please try again.");
    }
}