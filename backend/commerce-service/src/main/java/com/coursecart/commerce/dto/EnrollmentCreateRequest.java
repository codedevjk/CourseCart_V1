package com.coursecart.commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentCreateRequest {
    private Long userId;
    private Long courseId;
}
