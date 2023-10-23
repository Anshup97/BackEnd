package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
