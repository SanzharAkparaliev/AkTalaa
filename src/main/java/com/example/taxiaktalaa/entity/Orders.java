package com.example.taxiaktalaa.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odersId;
    private Integer price;
    private Date date;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;
}
