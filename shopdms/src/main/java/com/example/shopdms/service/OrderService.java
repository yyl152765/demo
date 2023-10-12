package com.example.shopdms.service;

import com.example.shopdms.entity.Goods;
import com.example.shopdms.entity.Order;
import com.example.shopdms.entity.OrderGoods;

import java.util.List;

public interface OrderService {

    //分页条件查询订单表
    List<Order> getlist(Order order,Integer start,Integer pageNum);

    //查询条件记录条数
    Object getordercount(Order order);

    //查询orderid查询商品订单表
    List<OrderGoods> getGoodtypeByorderrid(Integer orderid);


    Order findbyid(Integer id);

    boolean update(Order order);


}
