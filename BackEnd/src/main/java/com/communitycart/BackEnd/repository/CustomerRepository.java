package com.communitycart.BackEnd.repository;

import com.communitycart.BackEnd.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    public Customer findByEmailId(String email);
    public Customer deleteByEmailId(String email);
}
