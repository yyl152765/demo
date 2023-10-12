package com.example.shopdms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "o_caidou_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ordernumber;


    private Integer payprice;
    private Integer ispay;
    private Integer isship;
    @Temporal(TemporalType.DATE)
    private Date shiptime;
    private Integer isreceipts;
    private String shipnumber;
    private Integer state;
    @Temporal(TemporalType.DATE)
    private Date updatetime;
    private Integer iscovert;
    private Integer covert_caidou;
    private Integer covert_money;
    @Temporal(TemporalType.DATE)
    private Date createtime;
    private String openid;
    private String prepay_id;




}
