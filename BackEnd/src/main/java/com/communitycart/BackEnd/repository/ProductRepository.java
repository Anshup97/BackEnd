package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
      public Product findByProductName(String productName);
      public Product findByShopId(Long shopId);

}