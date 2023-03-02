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
@Table(name = "carrier")
public class Carrier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carrierId;

    private String carrierName;

    @ManyToMany
    private ArrayList<Region> regions;

    @ManyToMany
    private ArrayList<Place> places;

    @ManyToMany
    private ArrayList<Product> products;

    @OneToMany
    private ArrayList<Transaction> transactions;

    public boolean providesService(String pickupPlace, String deliveryPlace) {
        return this.getPlaces().contains(pickupPlace) && this.getPlaces().contains(deliveryPlace);
    }
}
