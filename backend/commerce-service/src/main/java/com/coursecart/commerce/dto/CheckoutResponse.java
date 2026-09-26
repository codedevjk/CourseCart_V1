package com.coursecart.commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
    private Long orderId;
    private String status;
    private String message;
}
