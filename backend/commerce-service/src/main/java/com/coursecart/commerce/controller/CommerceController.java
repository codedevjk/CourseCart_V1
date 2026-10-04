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

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        CheckoutResponse response = commerceService.processCheckout(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam("userId") Long userId) {
        List<OrderDTO> orders = commerceService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
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



