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
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long regionId;

    private String regionName;

    @OneToMany
    private ArrayList<Place> places;

    @ManyToMany
    private ArrayList<Carrier> carriers;
}
