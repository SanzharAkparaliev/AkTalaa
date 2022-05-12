package com.example.taxiaktalaa.contrller;

import com.example.taxiaktalaa.entity.Category;
import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.helper.Message;
import com.example.taxiaktalaa.repository.CategoryReposirory;
import com.example.taxiaktalaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CategoryReposirory categoryReposirory;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title","Ak-Talaa");
        List<Category> category =  categoryReposirory.findAll();
        System.out.println(category);
        model.addAttribute("categories",category);
        return "home";
    }

    @GetMapping("/signup")
    public String singup(Model model){
        model.addAttribute("title","Register - Smart Contact Manager");
        model.addAttribute("user",new User());
        return "signup";
    }

    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               @RequestParam(value = "agreement",
                                       defaultValue = "false")
    Boolean agreement, Model model, HttpSession session){
       try {
           if(!agreement){
               System.out.println("You have not agreed the terms and conditions");
               throw new Exception("You have not agreed the terms and conditions");
           }

           if(result.hasErrors()){
                model.addAttribute("user",user);
                return "signup";
           }

           user.setRole("ROLE_USER");
           user.setEnabled(true);
           user.setImageUrl("userlogo.png");
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           System.out.println("Agreement " + agreement);
           System.out.println("User " + user);
           model.addAttribute("user",new User());
           userRepository.save(user);
           session.setAttribute("message",new Message("Successfully Register !! " ,"alert-success"));
           return "signup";
       }catch (Exception e){
           e.printStackTrace();
           model.addAttribute("user",user);
           session.setAttribute("message",new Message("Something Went wrong !! " + e.getMessage(),"alert-danger"));
           return "signup";
       }
    }

    @GetMapping("/singin")
    public String customLogin(Model model){
        model.addAttribute("title","Login Page");
        return "login";
    }
}
