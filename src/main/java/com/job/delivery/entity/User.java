package com.job.delivery.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_generator")
    private Long userId;

    private String username;

    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
