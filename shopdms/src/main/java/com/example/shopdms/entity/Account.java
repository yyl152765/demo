package com.example.shopdms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_admin_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer t_admin_user;

    @Column(name = "login_admin")
    private String login_admin;
    @Column(name = "password_admin")
    private String password_admin;

}
