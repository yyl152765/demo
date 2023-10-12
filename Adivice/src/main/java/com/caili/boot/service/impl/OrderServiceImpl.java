package com.caili.boot.service.impl;

import com.caili.boot.dao.OrderMapper;
import com.caili.boot.entity.Order;
import com.caili.boot.service.OrderService;
import com.caili.boot.util.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> getorderbyopenid(String openid) {
        return orderMapper.getorderbyopenid(openid);
    }

    @Override
    public Integer saveOrder(Order order) {
        return orderMapper.saveOrder(order);
    }

    @Override
    public Integer updateOrder(Map<String,String> map) {
        String bank_type = map.get("bank_type");
        String is_subscribe = map.get("is_subscribe");
        String openid = map.get("openid");
        String out_trade_no = map.get("out_trade_no");
        String result_code = map.get("result_code");
        String total_fee = map.get("total_fee");
        String trade_type = map.get("trade_type");
        String transaction_id = map.get("transaction_id");
        String time_end = map.get("time_end");

        Order order=new Order();
        order.setOrdernumber(out_trade_no);
        order.setUpdatetime(CryptUtils.getnowtime());
        order.setIspay(1);
        System.out.println(order.toString());
        return orderMapper.updateOrder2(order);
    }

    @Override
    public Integer updateOrder2(Order order) {
        return orderMapper.updateOrder(order);
    }

    @Override
    public Integer deleteOrder(Order order) {
        return orderMapper.deleteOrder(order);
    }

    @Override
    public Order getGoodsbyordernumber(String ordernumber) {
        return orderMapper.getGoodsbyordernumber(ordernumber);
    }
}
