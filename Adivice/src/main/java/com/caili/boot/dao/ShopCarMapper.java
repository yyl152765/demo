package com.caili.boot.dao;

import com.caili.boot.entity.ShopCar;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShopCarMapper extends BaseDao<ShopCar>{

   List<ShopCar> getShopcarbyopenid(String openid);


   Integer deleteShopCar(ShopCar shopcar);
}
