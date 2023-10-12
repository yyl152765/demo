package com.example.shopdms.service;



import com.example.shopdms.entity.GoodsType;

import java.util.List;

public interface GoodsTypeService {

    //查询所有商品分类
    List<GoodsType> getGoodsTypelist();

    //删除分类
    Boolean deleteGoodsType(GoodsType goodsType);

    //添加商品分类
    Boolean SaveGoodsType(GoodsType goodsType);

    //根据主键查询商品分类
    GoodsType getGoodsTypeByid(Integer id);


}
