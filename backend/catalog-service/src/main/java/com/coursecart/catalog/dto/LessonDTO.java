package com.coursecart.catalog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private Long id;
    private Long courseId;
    private String title;
    private String content;
    private Integer displayOrder;
}
