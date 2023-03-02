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
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long placeId;

    private String placeName;

    @ManyToOne
    private Region region;

    @OneToMany
    private ArrayList<Offer> offers;

    @OneToMany
    private ArrayList<Request> requests;

    @ManyToMany
    private ArrayList<Carrier> carriers;


}
