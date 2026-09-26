package com.coursecart.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String content;
    
    @NotNull(message = "Display order is required")
    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder;
}
