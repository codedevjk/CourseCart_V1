package com.coursecart.catalog.dto;

import com.coursecart.catalog.entity.CourseStatus;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private CourseStatus status;
    private CategoryDTO category;

    private BigDecimal originalPrice;
    private String instructorName;
    private Double rating;
    private Integer ratingCount;
    private Boolean bestseller;
    
    private Integer lessonCount;
}
