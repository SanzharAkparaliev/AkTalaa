package com.example.taxiaktalaa.repository;

import com.example.taxiaktalaa.entity.Contact;
import com.example.taxiaktalaa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.ConfigAttribute;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    @Query("from Contact as c where c.user.userId =:userId")
    public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

    public List<Contact> findByNameContainingAndUser(String name, User user);
}
