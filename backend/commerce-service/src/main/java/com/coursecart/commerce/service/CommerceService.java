package com.coursecart.commerce.service;

import com.coursecart.commerce.dto.CheckoutRequest;
import com.coursecart.commerce.dto.CheckoutResponse;
import com.coursecart.commerce.dto.OrderDTO;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommerceService {
    CheckoutResponse processCheckout(CheckoutRequest request);
    List<OrderDTO> getOrdersByUserId(Long userId);
    List<OrderDTO> getRecentOrders(int limit);
    Page<OrderDTO> getAllOrders(Pageable pageable);
    BigDecimal calculateTotalRevenue();
}


