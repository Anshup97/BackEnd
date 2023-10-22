package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Cart;
import com.communitycart.BackEnd.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Category, Long> {
    public Cart findByProductId(Long productId);
    public Cart deleteByProductId(Long cartId);
}
