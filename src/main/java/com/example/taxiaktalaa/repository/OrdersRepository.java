package com.example.taxiaktalaa.repository;

import com.example.taxiaktalaa.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
