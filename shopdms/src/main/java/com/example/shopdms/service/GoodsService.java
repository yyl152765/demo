package com.example.shopdms.service;

import com.example.shopdms.entity.Goods;
import com.example.shopdms.entity.Index;


import java.util.List;

public interface GoodsService {
    //根据商品id查询某分类下商品信息
    List<Goods> getGoodsTypelist(Integer typeid);

    Boolean update(Goods goods);

    Boolean save(Goods goods);
    //根据主键查询
     Goods getGoodsType(Integer id);


     //查询主页轮播图
     Index getindex();

     //修改主页轮播图
     Boolean updateindex(String imgarr,String jumpurl);

}
