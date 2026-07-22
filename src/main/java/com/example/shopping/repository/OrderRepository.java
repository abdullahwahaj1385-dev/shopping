package com.example.shopping.repository;


import com.example.shopping.model.Customer;
import com.example.shopping.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderRepository extends JpaRepository<Order,Long> {
}
