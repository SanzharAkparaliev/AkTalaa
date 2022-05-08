package com.example.taxiaktalaa.service;

import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.entity.UserRole;

import java.util.Set;

public interface UserService {
    public User createUser(User user, Set<UserRole> roles) throws Exception;
}
