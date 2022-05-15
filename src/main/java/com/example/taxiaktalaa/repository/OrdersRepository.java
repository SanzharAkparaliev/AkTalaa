package com.example.taxiaktalaa.repository;

import com.example.taxiaktalaa.entity.Category;
import com.example.taxiaktalaa.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByCategory(Category category);
}
