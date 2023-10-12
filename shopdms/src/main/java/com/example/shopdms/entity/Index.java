package com.example.shopdms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "o_caidou_index")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String typename;

    private String imgarr;

    private String jumpurl;
}
