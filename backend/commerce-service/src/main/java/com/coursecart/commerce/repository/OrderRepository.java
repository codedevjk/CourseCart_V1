package com.coursecart.commerce.repository;

import com.coursecart.commerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    @Query("SELECT SUM(o.amountPaid) FROM Order o")
    BigDecimal calculateTotalRevenue();
}
