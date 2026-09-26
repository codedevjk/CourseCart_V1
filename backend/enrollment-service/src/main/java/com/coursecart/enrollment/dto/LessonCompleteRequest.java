package com.coursecart.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonCompleteRequest {
    @NotNull(message = "Completed status is required")
    private Boolean completed;
}
