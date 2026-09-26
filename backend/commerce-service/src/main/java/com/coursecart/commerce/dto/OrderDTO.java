package com.coursecart.commerce.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private BigDecimal amountPaid;
    private Date orderDate;
    private String paymentMethod;
}
