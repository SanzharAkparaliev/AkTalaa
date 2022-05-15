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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
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
        model.addAttribute("userName",user.getName());
        return "normal/user-dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("contact",new Contact());
        model.addAttribute("title","Add Contact");
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(
            @ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal,
            HttpSession session){
        try {
            String name = principal.getName();
            User user = userRepository.getUserByUserName(name);

            if(file.isEmpty()){
                contact.setImage("contact.png");
            }else {
                contact.setImage(file.getOriginalFilename());
                File saveFile  = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
            }
            contact.setUser(user);
            user.getContactList().add(contact);
            userRepository.save(user);
            //message success
            session.setAttribute("message",new Message("Your contact is added !! Add more ..","success"));
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message",new Message("Some went wrong !! Try again ..","danger"));

        }
        return "normal/add_contact_form";
    }


    @GetMapping("/show-contacts/{page}")
    public String showContact(@PathVariable("page") Integer page,Model model,Principal principal){
        model.addAttribute("title","Show User Contacts");
        String username = principal.getName();
        User user = userRepository.getUserByUserName(username);
        Pageable pageable = PageRequest.of(page,8);
        Page<Contact> contactList = contactRepository.findContactByUser(user.getUserId().intValue(),pageable);
        model.addAttribute("contacts",contactList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contactList.getTotalPages());
        return "normal/show_contacts";
    }

   @GetMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal){
       Optional<Contact> contactOptional = contactRepository.findById(cId);
       Contact contact = contactOptional.get();

       String userName = principal.getName();
       User user = userRepository.getUserByUserName(userName);

       if(user.getUserId() == contact.getUser().getUserId()) {
        model.addAttribute("title",contact.getName());
           model.addAttribute("contact", contact);
       }

       return "normal/contact_detail";
   }

    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId,Model model,HttpSession session,Principal principal){
        Optional<Contact> optionalContact = contactRepository.findById(cId);
        Contact contact = optionalContact.get();
        User user = userRepository.getUserByUserName(principal.getName());
        user.getContactList().remove(contact);
        userRepository.save(user);
        session.setAttribute("message",new Message("Contact deleted succesfully...","success"));
        return "redirect:/user/show-contacts/0";
    }

    @PostMapping("/update-contact/{cId}")
    public String updateForm(@PathVariable("cId") Integer cId,Model model){
        model.addAttribute("title","Update Contact");
        Contact contact = contactRepository.findById(cId).get();
        model.addAttribute("contact",contact);
        return "normal/update_form";
    }
    @PostMapping("/process-update")
    public String updateHandler(@ModelAttribute Contact contact,
                                @RequestParam("profileImage")MultipartFile file,
                                Model model,HttpSession session
            ,Principal principal){
        try {
            Contact oldContactDetail = contactRepository.findById(contact.getContactId().intValue()).get();
            if(!file.isEmpty()){
                //delete old photo
                File deleteFile  = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile,oldContactDetail.getImage());
                file1.delete();
                //update new photo
                File saveFile  = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());
            }else{
                contact.setImage(oldContactDetail.getImage());
            }
            User user = userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            contactRepository.save(contact);
            session.setAttribute("message",new Message("Your contact is updated..","success"));

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("contact Id" + contact.getContactId());
        return "redirect:/user/"+contact.getContactId()+"/contact";
    }

    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title","Profile Page");
        return "normal/profile";
    }

    @GetMapping("/settings")
    public String openSettings(){
        return "normal/settings";
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
    public String getOrders(Model model){
        List<Orders> orders = ordersRepository.findAll();
        model.addAttribute("title","All Orders");
        model.addAttribute("orders",orders);
        return "normal/orders";
    }

    @GetMapping("/orders/delete/{ordersId}")
    public String deleteOrders(@PathVariable("ordersId")Long id){
        ordersRepository.deleteById(id);
        return "redirect:/user/orders";
    }
}
