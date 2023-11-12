package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findByCustomerId(Long customerId);

    public List<Order> findBySellerId(Long sellerId);

    public Order findByOrderId(Long orderId);
}
