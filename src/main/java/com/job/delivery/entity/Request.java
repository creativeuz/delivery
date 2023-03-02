package com.job.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;

    @ManyToOne
    private Place place;

    @ManyToMany
    private ArrayList<Product> products;

    @OneToOne
    private Transaction transaction;
}
