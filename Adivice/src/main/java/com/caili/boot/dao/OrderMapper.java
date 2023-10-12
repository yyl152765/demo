package com.caili.boot.dao;

import com.caili.boot.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    //查询用户订单
    List<Order> getorderbyopenid(String openid);
    //添加订单
    Integer saveOrder(Order order);
    //修改订单状态
    Integer updateOrder(Order order);
    //修改订单状态
    Integer updateOrder2(Order order);
    //修改订单状态
    Integer deleteOrder(Order order);

    Order getGoodsbyordernumber(String ordernumber);

}
