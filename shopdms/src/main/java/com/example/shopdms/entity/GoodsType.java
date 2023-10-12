package com.example.shopdms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "o_caidou_goods_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String types;
    private String goodsname;
    private Integer state;
    @Temporal(TemporalType.DATE)
    private Date createtime;
    private String image;


}
