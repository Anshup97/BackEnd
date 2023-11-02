package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findProductBySellerIdAndCategoryId(Long sellerId, Long categoryId);

    public Product findProductByProductId(Long productId);
}
