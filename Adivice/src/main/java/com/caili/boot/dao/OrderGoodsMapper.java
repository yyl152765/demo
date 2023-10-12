package com.caili.boot.dao;

import com.caili.boot.entity.Order;
import com.caili.boot.entity.OrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface OrderGoodsMapper {
    //查询订单详情
    List<OrderGoods> getordergoogsbynumber(String orderid);
    //添加订单(多表)
    Integer saveOrderGoods(List<OrderGoods> orderGoods);
    //修改订单
    Integer updateOrderGoods(OrderGoods orderGoods);
}
