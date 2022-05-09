package com.example.taxiaktalaa;

import com.example.taxiaktalaa.entity.Role;
import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.entity.UserRole;
import com.example.taxiaktalaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TaxiAkTalaaApplication {
   @Autowired
   private UserService userService;
    public static void main(String[] args) {
        SpringApplication.run(TaxiAkTalaaApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        User user = new User();
//        user.setFirstName("Sanzhar");
//        user.setLastName("Taalaybek uulu");
//        user.setEmail("taalaibekov@gmail.com");
//        user.setCarInfo("lexus rx 350");
//        user.setPhone("776262328");
//
//        Role role = new Role();
//        role.setRoleId(1L);
//        role.setRoleName("ADMIN");
//
//        Set<UserRole> roles = new HashSet<>();
//        UserRole userRole = new UserRole();
//        userRole.setUser(user);
//        userRole.setRole(role);
//
//        roles.add(userRole);
//        User user1 = userService.createUser(user,roles);
//        System.out.println(user1.toString());
//    }
}
