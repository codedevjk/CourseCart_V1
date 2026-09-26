package com.coursecart.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
}
