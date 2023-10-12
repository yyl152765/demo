package com.caili.boot.service;

import com.caili.boot.entity.ShopCar;

import java.util.List;
import java.util.Map;

public interface ShopCarService {

    //查询我的购物车
    List<ShopCar> getShopCarbyopenid(String openid);

    //查询我的购物车
    List<Map<String,String>> getShopCarbyopenidandorderid(String openid,String goodsid);
    Integer getCountByopenidandgoodsid(String openid,String goodsid);

    Integer deleteShopCar(ShopCar shopcar);

    //查询我的购物车商品的数量
    Integer getCountByopenid(String openid);


    Integer saveMyshopcar(ShopCar shopCar);

    Integer updateshopcar(ShopCar shopCar);
}
