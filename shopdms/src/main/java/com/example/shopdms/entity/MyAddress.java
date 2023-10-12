package com.example.shopdms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "o_caidou_myaddress")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String openid;
    private String shname;
    private String phone;
    private String area;
    private String areadetail;
    private Integer isdefault;


}
