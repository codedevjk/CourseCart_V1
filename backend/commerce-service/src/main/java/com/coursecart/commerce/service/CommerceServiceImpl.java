package com.coursecart.commerce.service;

import com.coursecart.commerce.client.CatalogServiceClient;
import com.coursecart.commerce.client.EnrollmentServiceClient;
import com.coursecart.commerce.dto.CheckoutRequest;
import com.coursecart.commerce.dto.CheckoutResponse;
import com.coursecart.commerce.dto.CourseDTO;
import com.coursecart.commerce.dto.EnrollmentCreateRequest;
import com.coursecart.commerce.dto.OrderDTO;
import com.coursecart.commerce.entity.Order;
import com.coursecart.commerce.exception.CommerceServiceException;
import com.coursecart.commerce.exception.ErrorMessages;
import org.springframework.http.HttpStatus;
import com.coursecart.commerce.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommerceServiceImpl implements CommerceService {

    private final OrderRepository orderRepository;
    private final CatalogServiceClient catalogServiceClient;
    private final EnrollmentServiceClient enrollmentServiceClient;
    private final ModelMapper modelMapper;

    @Autowired
    public CommerceServiceImpl(OrderRepository orderRepository,
                               CatalogServiceClient catalogServiceClient,
                               EnrollmentServiceClient enrollmentServiceClient,
                               ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.catalogServiceClient = catalogServiceClient;
        this.enrollmentServiceClient = enrollmentServiceClient;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CheckoutResponse processCheckout(CheckoutRequest request) {
        CourseDTO course = catalogServiceClient.getCourseById(request.getCourseId());

        if (!"ACTIVE".equals(course.getStatus())) {
            throw new CommerceServiceException(HttpStatus.BAD_REQUEST, ErrorMessages.COURSE_UNAVAILABLE);
        }

        boolean isEnrolled = enrollmentServiceClient.checkEnrollment(request.getUserId(), request.getCourseId());
        if (isEnrolled) {
            throw new CommerceServiceException(HttpStatus.CONFLICT, ErrorMessages.ALREADY_ENROLLED);
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
             throw new CommerceServiceException(HttpStatus.BAD_REQUEST, "Payment method is required");
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setCourseId(request.getCourseId());
        order.setAmountPaid(course.getPrice());
        order.setPaymentMethod(request.getPaymentMethod());
        
        Order savedOrder = orderRepository.save(order);

        EnrollmentCreateRequest enrollmentReq = new EnrollmentCreateRequest(request.getUserId(), request.getCourseId());
        enrollmentServiceClient.createEnrollment(enrollmentReq);

        return new CheckoutResponse(savedOrder.getId(), "SUCCESS", "Enrolled");
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getRecentOrders(int limit) {
        return orderRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "orderDate")))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "orderDate"));
        return orderRepository.findAll(sortedPageable).map(this::convertToDTO);
    }

    @Override
    public BigDecimal calculateTotalRevenue() {
        BigDecimal revenue = orderRepository.calculateTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    private OrderDTO convertToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
