package com.coursecart.commerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "order_date", nullable = false, updatable = false)
    private Timestamp orderDate;

    @Column(name = "payment_method")
    private String paymentMethod;
    
    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = Timestamp.from(Instant.now());
        }
    }
}
