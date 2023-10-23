package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Category;
import com.communitycart.BackEnd.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    public Seller findByEmail(String email);
    public Seller deleteByEmail(String email);


}
