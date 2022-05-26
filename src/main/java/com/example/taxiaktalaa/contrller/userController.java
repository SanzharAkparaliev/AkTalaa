package com.example.taxiaktalaa.contrller;

import com.example.taxiaktalaa.entity.Category;
import com.example.taxiaktalaa.entity.Contact;
import com.example.taxiaktalaa.entity.Orders;
import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.helper.Message;
import com.example.taxiaktalaa.repository.CategoryReposirory;
import com.example.taxiaktalaa.repository.ContactRepository;
import com.example.taxiaktalaa.repository.OrdersRepository;
import com.example.taxiaktalaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryReposirory categoryReposirory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private OrdersRepository ordersRepository;
    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user",user);
    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title","User Dashboard");
        User  user = userRepository.getUserByUserName(principal.getName());
        System.out.println(principal.getName());
        model.addAttribute("user",user);
        return "normal/user-dashboard";
    }






    @PostMapping("/process-update")
    public String updateHandler(@RequestParam("oldName") String newCarName,
                                @RequestParam("profileImage")MultipartFile file,
                                Model model,HttpSession session
            ,Principal principal){
        try {
            User user = userRepository.getUserByUserName(principal.getName());
            if(!file.isEmpty()){
                //delete old photo
                File deleteFile  = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile,user.getImageUrl());
                file1.delete();
                //update new photo
                File saveFile  = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
                user.setImageUrl(file.getOriginalFilename());
            }else{
                user.setImageUrl(user.getImageUrl());
            }

            userRepository.save(user);

            session.setAttribute("message",new Message("Your car info is updated..","success"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/index";
    }

    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title","Profile Page");
        return "normal/profile";
    }

    @GetMapping("/settings")
    public String openSettings(){
        return "normal/setting";
    }


    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                Principal principal,
                                 HttpSession session
    ){
        String userName = principal.getName();
        User currentUser  = userRepository.getUserByUserName(userName);

        if(passwordEncoder.matches(oldPassword,currentUser.getPassword())){
            //change password
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentUser);
            session.setAttribute("message",new Message("Your password is successfully changed","success"));
        }else{
            //error
            session.setAttribute("message",new Message("Please enter correct old password !!","danger"));
            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }

    @GetMapping("/add-orders")
    public String addOders(Model model){
        List<Category> category = categoryReposirory.findAll();
        model.addAttribute("title","Add Oders");
        model.addAttribute("orders",new Orders());
        model.addAttribute("categories",category);
        return "/normal/add-oders";
    }
    @PostMapping("/add-orders")
    public String saveOders(@ModelAttribute("oders")Orders orders,Model model,Principal principal){
        List<Category> category = categoryReposirory.findAll();

        Orders newOrders = new Orders();
        newOrders.setUser(userRepository.getUserByUserName(principal.getName()));
        newOrders.setCategory(orders.getCategory());
        newOrders.setDate(orders.getDate());
        newOrders.setPlace(orders.getPlace());
        newOrders.setPrice(orders.getPrice());
        ordersRepository.save(newOrders);
        return "redirect:/user/orders";
    }

    @GetMapping("/orders")
    public String getOrders(Model model,Principal principal){
        User user = userRepository.getUserByUserName(principal.getName());
        List<Orders> orders = user.getOrders();
        model.addAttribute("title","All Orders");
        model.addAttribute("orders",orders);
        System.out.println(orders);
        return "normal/orders";
    }

    @GetMapping("/orders/delete/{ordersId}")
    public String deleteOrders(@PathVariable("ordersId")Long id){
        ordersRepository.deleteById(id);
        return "redirect:/user/orders";
    }




}
