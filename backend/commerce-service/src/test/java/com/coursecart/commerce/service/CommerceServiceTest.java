package com.coursecart.commerce.service;

import com.coursecart.commerce.client.CatalogServiceClient;
import com.coursecart.commerce.client.EnrollmentServiceClient;
import com.coursecart.commerce.dto.CheckoutRequest;
import com.coursecart.commerce.dto.CheckoutResponse;
import com.coursecart.commerce.dto.CourseDTO;
import com.coursecart.commerce.dto.EnrollmentCreateRequest;
import com.coursecart.commerce.dto.EnrollmentDTO;
import com.coursecart.commerce.dto.OrderDTO;
import com.coursecart.commerce.entity.Order;
import com.coursecart.commerce.exception.CommerceServiceException;
import com.coursecart.commerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommerceServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogServiceClient catalogServiceClient;

    @Mock
    private EnrollmentServiceClient enrollmentServiceClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommerceServiceImpl commerceService;

    private CourseDTO activeCourse;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        activeCourse = new CourseDTO();
        activeCourse.setId(10L);
        activeCourse.setTitle("Java 101");
        activeCourse.setPrice(new BigDecimal("19.99"));
        activeCourse.setStatus("ACTIVE");

        savedOrder = new Order();
        savedOrder.setId(50L);
        savedOrder.setUserId(1L);
        savedOrder.setCourseId(10L);
        savedOrder.setAmountPaid(new BigDecimal("19.99"));
        savedOrder.setPaymentMethod("Credit Card");
    }

    @Test
    void testProcessCheckout_Success_ActiveCourse_NotEnrolled_ValidPayment() {
        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCourseId(10L);
        request.setPaymentMethod("CARD");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(enrollmentServiceClient.createEnrollment(any(EnrollmentCreateRequest.class))).thenReturn(new EnrollmentDTO());

        CheckoutResponse response = commerceService.processCheckout(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(50L, response.getOrderId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(enrollmentServiceClient, times(1)).createEnrollment(any(EnrollmentCreateRequest.class));
    }

    @Test
    void testProcessCheckout_InactiveCourse_ThrowsBadRequestException() {
        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCourseId(10L);
        request.setPaymentMethod("CARD");
        
        CourseDTO inactiveCourse = new CourseDTO();
        inactiveCourse.setId(10L);
        inactiveCourse.setTitle("Java 101");
        inactiveCourse.setPrice(new BigDecimal("19.99"));
        inactiveCourse.setStatus("INACTIVE");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(inactiveCourse);

        assertThrows(CommerceServiceException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_DraftCourse_ThrowsBadRequestException() {
        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCourseId(10L);
        request.setPaymentMethod("UPI");
        
        CourseDTO draftCourse = new CourseDTO();
        draftCourse.setId(10L);
        draftCourse.setTitle("Java 101");
        draftCourse.setPrice(new BigDecimal("19.99"));
        draftCourse.setStatus("DRAFT");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(draftCourse);

        assertThrows(CommerceServiceException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_AlreadyEnrolled_ThrowsConflictException() {
        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCourseId(10L);
        request.setPaymentMethod("CARD");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(true);

        assertThrows(CommerceServiceException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_NullPaymentMethod_ThrowsBadRequestException() {
        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCourseId(10L);
        request.setPaymentMethod(null);

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(false);

        assertThrows(CommerceServiceException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_BlankPaymentMethod_ThrowsBadRequestException() {
        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCourseId(10L);
        request.setPaymentMethod("   ");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(false);

        assertThrows(CommerceServiceException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testGetOrdersByUserId_ReturnsMappedOrderDTOs() {
        Order order = new Order();
        order.setId(50L);
        order.setUserId(1L);
        order.setCourseId(10L);
        order.setAmountPaid(new BigDecimal("19.99"));
        order.setPaymentMethod("Credit Card");
        
        when(orderRepository.findByUserIdOrderByOrderDateDesc(1L)).thenReturn(Collections.singletonList(order));
        
        OrderDTO dto = new OrderDTO();
        dto.setId(50L);
        dto.setAmountPaid(new BigDecimal("19.99"));
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(dto);

        List<OrderDTO> result = commerceService.getOrdersByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(50L, result.get(0).getId());
        assertEquals(new BigDecimal("19.99"), result.get(0).getAmountPaid());
    }

    @Test
    void testCalculateTotalRevenue_ReturnsSum() {
        when(orderRepository.calculateTotalRevenue()).thenReturn(new BigDecimal("199.95"));

        BigDecimal revenue = commerceService.calculateTotalRevenue();

        assertEquals(new BigDecimal("199.95"), revenue);
    }

    @Test
    void testCalculateTotalRevenue_NoOrders_ReturnsZero() {
        when(orderRepository.calculateTotalRevenue()).thenReturn(null);

        BigDecimal revenue = commerceService.calculateTotalRevenue();

        assertEquals(BigDecimal.ZERO, revenue);
    }
}
