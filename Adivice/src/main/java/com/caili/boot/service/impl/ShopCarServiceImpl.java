package com.caili.boot.service.impl;

import com.caili.boot.dao.MyAddressMapper;
import com.caili.boot.dao.ShopCarMapper;
import com.caili.boot.entity.ShopCar;
import com.caili.boot.service.ShopCarService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ShopCarServiceImpl implements ShopCarService {
    static Logger logger = LoggerFactory.getLogger(ShopCarServiceImpl.class.getName());

    static String tablename="o_caidou_shopcar";//表名
    @Autowired
    private ShopCarMapper shopCarMapper;


    @Override
    public List<ShopCar> getShopCarbyopenid(String openid) {
        logger.info("select *  from "+tablename+" where openid="+openid);
 /*       Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);*/
        return shopCarMapper.getShopcarbyopenid(openid);
    }

    @Override
    public List<Map<String,String>> getShopCarbyopenidandorderid(String openid, String goodsid) {
        logger.info("select count from"+tablename+" where openid="+openid);
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        prammap.put("goodsid",goodsid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions+"and state!=-1");
        map.put("pages","");
        return shopCarMapper.list(map);
    }

    @Override
    public Integer deleteShopCar(ShopCar shopcar) {
        logger.info("批量删除购物车"+shopcar.toString());
        return shopCarMapper.deleteShopCar(shopcar);
    }

    @Override
    public Integer getCountByopenid(String openid) {
        logger.info("select count from"+tablename+" where openid="+openid);
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions+"and state!=-1");

        return shopCarMapper.getCountByStr(map);
    }

    @Override
    public Integer getCountByopenidandgoodsid(String openid,String goodsid) {
        logger.info("select count from"+tablename+" where openid="+openid);
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        prammap.put("goodsid",goodsid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions+"and state!=-1");
        return shopCarMapper.getCountByStr(map);
    }


    @Override
    public Integer saveMyshopcar(ShopCar shopCar) {
        return shopCarMapper.save(shopCar);
    }

    @Override
    public Integer updateshopcar(ShopCar shopCar) {
        return shopCarMapper.update(shopCar);
    }
}
