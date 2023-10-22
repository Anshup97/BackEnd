package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByOrderId(Long orderId);
    public Order findByStatus(Order.Status status);
    public Order findByCustomerId(Long customerId);

}
