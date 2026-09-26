package com.coursecart.commerce.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponse {
    private BigDecimal totalRevenue;
}
