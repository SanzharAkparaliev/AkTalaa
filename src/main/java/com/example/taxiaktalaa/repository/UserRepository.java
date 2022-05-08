package com.example.taxiaktalaa.repository;

import com.example.taxiaktalaa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByFirstName(String userName);
}
