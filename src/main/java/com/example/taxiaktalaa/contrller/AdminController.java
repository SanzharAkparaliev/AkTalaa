package com.example.taxiaktalaa.contrller;

import com.example.taxiaktalaa.entity.Category;
import com.example.taxiaktalaa.repository.CategoryReposirory;
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

    @GetMapping("/categories")
    public String allCategory(Model model){
        List<Category> category =  categoryReposirory.findAll();
        model.addAttribute("categories",category);
        return "admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id")Long id){
        categoryReposirory.deleteById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable("id")Long id,Model model){
        Optional<Category> category = categoryReposirory.findById(id);
        if(category.isPresent()){
            model.addAttribute("category",category.get());
            return "admin/add-category";
        }else
            return  "404";
    }
}
