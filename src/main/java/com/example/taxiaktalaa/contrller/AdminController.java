package com.example.taxiaktalaa.contrller;

import com.example.taxiaktalaa.entity.Category;
import com.example.taxiaktalaa.entity.Orders;
import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.repository.CategoryReposirory;
import com.example.taxiaktalaa.repository.OrdersRepository;
import com.example.taxiaktalaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryReposirory categoryReposirory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping("/")
    public String adminPage(Model model){
        model.addAttribute("title","Admin Page");
        return "admin/admin-dashboard";
    }

    @GetMapping("/add-categories")
    public String getCategoryPage(Model model){
        model.addAttribute("title","Add Category");
        model.addAttribute("category",new Category());
        return "admin/add-category";
    }

    @PostMapping("/add-category")
    public String addCategoru(@ModelAttribute("category")Category category){
        categoryReposirory.save(category);
        return "redirect:/admin/categories";
    }
    @PostMapping("/update-category")
    public String updateCategoru(@ModelAttribute("category")Category category){
        Category category1 = categoryReposirory.findById(category.getCId()).get();
        category1.setCatName(category.getCatName());
        categoryReposirory.save(category1);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories")
    public String allCategory(Model model){
        List<Category> category =  categoryReposirory.findAll();
        model.addAttribute("categories",category);
        return "admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id")Long id){
        try {
            Optional<Category> category = categoryReposirory.findById(id);
            categoryReposirory.deleteById(id);
            return "redirect:/admin/categories";
        }catch (Exception ex){
            return "404";
        }
    }

    @GetMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable("id")Long id,Model model){
        Optional<Category> category = categoryReposirory.findById(id);
        if(category.isPresent()){
            model.addAttribute("category",category.get());
            return "admin/update-category";
        }else
            return  "404";
    }
    @GetMapping("/drivers")
    public String allDrivers(Model model){
        model.addAttribute("title","All Drivers");
        List<User> user = (List<User>) userRepository.findByRole("ROLE_USER");
        model.addAttribute("users",user);
        return  "admin/drivers";
    }

    @GetMapping("/orders")
    public String allOrders(Model model){
        model.addAttribute("title","All Orders");
        List<Orders> orders = ordersRepository.findAll();
        model.addAttribute("orders",orders);
        return "admin/orders";
    }

    @GetMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable Integer id){
        try {
            Optional<Orders> orders = ordersRepository.findById(Long.valueOf(id));
            ordersRepository.deleteById(Long.valueOf(id));
            return "redirect:/admin/orders";
        }catch (Exception e){
            return "404";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id){
        try {
            Optional<User> user = userRepository.findById(id);
            userRepository.deleteById(id);
            return "redirect:/admin/drivers";
        }catch (Exception e){
            return "404";
        }
    }
}
