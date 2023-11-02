package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public CartItem deleteByCartId(Long cartId);
}
