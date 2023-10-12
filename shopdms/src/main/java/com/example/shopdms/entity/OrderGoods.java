package com.example.shopdms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "o_caidou_ordergoods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer orderid;

    private Integer goodsid;

    private Integer addressid;

    private Integer goodsnum;

    private Integer goodsprice;

    private Integer state;
    @Temporal(TemporalType.DATE)
    private Date createtime;
    @Temporal(TemporalType.DATE)
    private Date updatetime;
}
