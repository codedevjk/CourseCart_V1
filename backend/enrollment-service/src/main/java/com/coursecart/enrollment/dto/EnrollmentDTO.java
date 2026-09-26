package com.coursecart.enrollment.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private Date enrolledAt;
    private Integer progressPercentage;
}
