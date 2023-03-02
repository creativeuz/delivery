package com.job.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long productId;

    private String productName;

    @ManyToMany
    private ArrayList<Offer> offers;

    @ManyToMany
    private ArrayList<Request> requests;

}
