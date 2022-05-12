package com.example.taxiaktalaa.config;


import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     User user = userRepository.getUserByUserName(username);

     if(user == null){
         throw  new UsernameNotFoundException("Could not found  user!!");
     }
     CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails;
    }
}
