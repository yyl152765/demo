package com.caili.boot.service;

import com.caili.boot.entity.Order;
import com.caili.boot.entity.OrderGoods;
import org.mockito.internal.matchers.Or;

import java.util.List;
import java.util.Map;

public interface OrderService {
    //查询用户订单
    List<Order> getorderbyopenid(String openid);
    //添加订单
    Integer saveOrder(Order order);
    //修改订单状态
    Integer updateOrder(Map<String,String> order);

    //修改订单状态
    Integer updateOrder2(Order order);

    //修改订单状态
    Integer deleteOrder(Order order);
    Order getGoodsbyordernumber(String ordernumber);
}
