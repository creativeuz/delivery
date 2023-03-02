package com.job.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    private int score;

    @OneToOne
    private Request request;

    @OneToOne
    private Offer offer;

    @ManyToOne
    private Carrier carrier;

    @ManyToOne
    private Product product;

/*
i made some changes to entities so you should rewrite addTransaction api by this:
1. Transaction.class has Long transactionId, Request request, Offer offer, Carrier carrier, Product product data fields.
2. Request.class has Long requestId, String placeName , ArrayList<Product> products, Transaction transaction data fields
3. Offer.class has Long offerId, Place place, ArrayList<Product> products, Transaction transaction data fields
4. Product.class has  Long productId, String productName, ArrayList<Offer> offers, ArrayList<Request> requests data fields
5. Region.class has Long regionId, String regionName, ArrayList<Place> places, ArrayList<Carrier> carriers data fields
6. Place.class Long placeId, String placeName, Region region, ArrayList<Offer> offers, ArrayList<Request> requests


    private Long placeId;

    private String placeName;

    @ManyToOne
    private Region region;

    @OneToMany
    private ArrayList<Offer> offers;

    @OneToMany
    private ArrayList<Request> requests;

*/




}



