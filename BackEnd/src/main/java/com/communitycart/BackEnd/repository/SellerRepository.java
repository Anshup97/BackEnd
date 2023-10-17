package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    public Seller findByEmail(String email);
    public Seller deleteByEmail(String email);
}
