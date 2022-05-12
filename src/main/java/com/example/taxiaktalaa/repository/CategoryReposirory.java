package com.example.taxiaktalaa.repository;

import com.example.taxiaktalaa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryReposirory extends JpaRepository<Category,Long> {
}
