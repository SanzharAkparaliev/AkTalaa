package com.example.taxiaktalaa.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odersId;
    private Integer price;
    private Date date;
    private Integer place;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;
}
