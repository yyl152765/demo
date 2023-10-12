package com.caili.boot.service;

import com.caili.boot.entity.OrderGoods;

import java.util.List;

public interface OrderGoodsService {
    //查询订单详情
    List<OrderGoods> getordergoogsbynumber(String ordernumber);
    //添加订单
    Integer saveOrderGoods(List<OrderGoods> orderGoods);
    //修改订单
    Integer updateOrderGoods(OrderGoods orderGoods);
}
