package com.example.taxiaktalaa.contrller;

import com.example.taxiaktalaa.entity.Contact;
import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.repository.ContactRepository;
import com.example.taxiaktalaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/search/{query}")
//    public ResponseEntity<?> search(@PathVariable("query") String  query, Principal principal){
//
//        User user  = userRepository.getUserByUserName(principal.getName());
//        List<Contact> contactList = contactRepository.findByNameContainingAndUser(query,user);
//        return ResponseEntity.ok(contactList);
//    }
}
