package com.example.shopdms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "o_caidou_goods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String goodsname;
    private String specifications;
    private String details;
    private String titleimage;
    private String img;
    private String imgarr;
    private Integer retailprice;
    private Integer favorableprice;
    private Integer caidounum;
    private Integer cadouparice;
    @Temporal(TemporalType.DATE)
    private Date createtime;
    private Integer state;

    private Integer typeid;


}
