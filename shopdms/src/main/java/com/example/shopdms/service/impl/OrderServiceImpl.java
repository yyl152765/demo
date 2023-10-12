package com.example.shopdms.service.impl;

import com.example.shopdms.entity.GoodsType;
import com.example.shopdms.entity.MyAddress;
import com.example.shopdms.entity.Order;
import com.example.shopdms.entity.OrderGoods;
import com.example.shopdms.repository.BaseRepository;
import com.example.shopdms.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.LinkedHashMap;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class.getName());
    @Autowired
    private BaseRepository<Order,Integer> baseRepository;

    @Autowired
    private BaseRepository<OrderGoods,Integer> ordergoodsdao;
    static final String TABLENAME="Order";

    @Override
    public List<Order> getlist(Order order,Integer start,Integer pageNum) {
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();

        if(order.getOrdernumber()!=null){
            map.put("ordernumber like","%"+order.getOrdernumber()+"%");
        }
        if(order.getIspay()!=null){
            map.put("ispay=",order.getIspay());
        }
        if(order.getIsship()!=null){
            map.put("isship=",order.getIsship());
        }
        if(order.getShipnumber()!=null){
            map.put("shipnumber like","%"+order.getShipnumber()+"%");
        }
        if(order.getCreatetime()!=null){
            map.put("createtime>=",order.getCreatetime());
        }
        if(order.getUpdatetime()!=null){
            map.put("createtime<=",order.getUpdatetime());
        }
        if(order.getIscovert()!=null){
            map.put("iscovert=",order.getIscovert());
        }
        if(order.getOpenid()!=null){
            map.put("openid=",order.getOpenid());
        }
        map.put("state !=",-1);
        return baseRepository.findByMoreFiledpages(TABLENAME,map,start,pageNum);
    }

    @Override
    public Object getordercount(Order order) {
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();

        if(order.getOrdernumber()!=null){
            map.put("ordernumber like","%"+order.getOrdernumber()+"%");
        }
        if(order.getIspay()!=null){
            map.put("ispay=",order.getIspay());
        }
        if(order.getIsship()!=null){
            map.put("isship=",order.getIsship());
        }
        if(order.getShipnumber()!=null){
            map.put("shipnumber like","%"+order.getShipnumber()+"%");
        }
        if(order.getCreatetime()!=null){
            map.put("createtime>=",order.getCreatetime());
        }
        if(order.getUpdatetime()!=null){
            map.put("createtime<=",order.getUpdatetime());
        }
        if(order.getIscovert()!=null){
            map.put("iscovert=",order.getIscovert());
        }
        if(order.getOpenid()!=null){
            map.put("openid=",order.getOpenid());
        }
        map.put("state !=",-1);

        return  baseRepository.findCount2(TABLENAME,map);
    }

    @Override
    public List<OrderGoods> getGoodtypeByorderrid(Integer orderid) {
        logger.info("select * from OrderGoods where orderid="+orderid+" and state ="+1);
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("orderid=",orderid);
        map.put("state=",1);
        return ordergoodsdao.findByMoreFiled("OrderGoods",map);
    }

    @Override
    public Order findbyid(Integer id) {
        logger.info("select * from order where id="+id);

        return baseRepository.findByid(new Order(),id);
    }

    @Override
    public boolean update(Order order) {
        logger.info("录入单号shipnumber="+order.getShipnumber());
        return baseRepository.update(order);
    }
}
