package com.coursecart.catalog.dto;

import com.coursecart.catalog.entity.CourseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatusRequest {
    @NotNull(message = "Status is required")
    private CourseStatus status;
}
