package com.example.taxiaktalaa.service.impl;

import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.entity.UserRole;
import com.example.taxiaktalaa.repository.RoleRepository;
import com.example.taxiaktalaa.repository.UserRepository;
import com.example.taxiaktalaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
    @Override
    public User createUser(User user, Set<UserRole> roles) throws Exception {
        User local =  userRepository.findByFirstName(user.getFirstName());

        if(local != null){
            throw new Exception("User already present!");
        }else {
            for(UserRole ur:roles){
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(roles);
            local = userRepository.save(user);
        }

        return local;
    }
}
