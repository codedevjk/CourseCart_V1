package com.coursecart.commerce.controller;

import com.coursecart.commerce.dto.CheckoutRequest;
import com.coursecart.commerce.dto.CheckoutResponse;
import com.coursecart.commerce.dto.OrderDTO;
import com.coursecart.commerce.dto.RevenueResponse;
import com.coursecart.commerce.service.CommerceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/commerce")
public class CommerceController {

    private final CommerceService commerceService;

    @Autowired
    public CommerceController(CommerceService commerceService) {
        this.commerceService = commerceService;
    }

    // TODO[TRAINEE]: Map to POST /checkout and add validation annotations
    public ResponseEntity<CheckoutResponse> checkout(CheckoutRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement checkout");
    }

    // TODO[TRAINEE]: Map to GET /orders and add request parameter annotation
    public ResponseEntity<List<OrderDTO>> getOrders(Long userId) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Implement getOrders");
    }

    @GetMapping("/orders/recent")
    public ResponseEntity<List<OrderDTO>> getRecentOrders(@RequestParam(value = "limit", defaultValue = "5") int limit) {
        List<OrderDTO> orders = commerceService.getRecentOrders(limit);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/all")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        Page<OrderDTO> orders = commerceService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueResponse> getRevenue() {
        RevenueResponse response = new RevenueResponse(commerceService.calculateTotalRevenue());
        return ResponseEntity.ok(response);
    }
}



