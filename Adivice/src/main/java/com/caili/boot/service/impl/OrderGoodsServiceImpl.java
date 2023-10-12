package com.caili.boot.service.impl;

import com.caili.boot.dao.OrderGoodsMapper;
import com.caili.boot.entity.OrderGoods;
import com.caili.boot.service.OrderGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    public List<OrderGoods> getordergoogsbynumber(String ordernumber) {
        return orderGoodsMapper.getordergoogsbynumber(ordernumber);
    }

    @Override
    public Integer saveOrderGoods(List<OrderGoods> orderGoods) {
        return orderGoodsMapper.saveOrderGoods(orderGoods);
    }

    @Override
    public Integer updateOrderGoods(OrderGoods orderGoods) {
        return orderGoodsMapper.updateOrderGoods(orderGoods);
    }
}
