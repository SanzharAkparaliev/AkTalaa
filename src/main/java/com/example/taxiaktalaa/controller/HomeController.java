package com.example.taxiaktalaa.controller;

import com.example.taxiaktalaa.entity.Role;
import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.entity.UserRole;
import com.example.taxiaktalaa.helper.Message;
import com.example.taxiaktalaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private UserService userService;
    @GetMapping
    public String home(){
        return "index";
    }

    @GetMapping("/singup")
    public String registrUser(Model model){
        model.addAttribute("title","Register - Ak Talaa Taxi");
        model.addAttribute("user",new User());
        return "singup";
    }

    @PostMapping("/do_register")
    public String createUser(@Valid  @ModelAttribute("user") User user,
                           BindingResult result,Model model, HttpSession session) throws Exception {
       try {
           if(result.hasErrors()){
               model.addAttribute("user",user);
               return "singup";
           }
           user.setPassword("password");
           Role role = new Role();
           role.setRoleId(2L);
           role.setRoleName("NORMAL");
           Set<UserRole> userRoleSet = new HashSet<>();
           UserRole userRole = new UserRole();
           userRole.setRole(role);
           userRole.setUser(user);
           userRoleSet.add(userRole);
           model.addAttribute("user",new User());
           userService.createUser(user,userRoleSet);
           session.setAttribute("message",new Message("Succesfully Register !!","alert-success"));
           return "singup";
       }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message",new Message("Something went wrong !!" + e.getMessage(),"alert-danger"));
            return "singup";
       }

    }

}
