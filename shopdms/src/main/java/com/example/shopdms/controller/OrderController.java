package com.example.shopdms.controller;

import com.example.shopdms.entity.Goods;
import com.example.shopdms.entity.MyAddress;
import com.example.shopdms.entity.Order;
import com.example.shopdms.entity.OrderGoods;
import com.example.shopdms.service.GoodsService;
import com.example.shopdms.service.MyAddressService;
import com.example.shopdms.service.OrderService;
import com.example.shopdms.util.ResponseMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("shopdms")
public class OrderController {
    static Logger logger = LoggerFactory.getLogger(OrderController.class.getName());


    @Autowired
    private OrderService orderService;








    @ResponseBody
    @RequestMapping("getorderlist")
    public Map<String, Object> getorderlist(@RequestParam String ordernumber, @RequestParam String isship, @RequestParam String shipnumber,
                                            @RequestParam String ispay, @RequestParam String createtime, @RequestParam String endtime, @RequestParam String usernumber,@RequestParam String iscovert,
                                            @RequestParam("page") Integer pages, @RequestParam("limit") Integer limits, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Order order = new Order();
        if(!ordernumber.equals("")){
            order.setOrdernumber(ordernumber);
        }

        if(!isship.equals("")){
            order.setIsship(Integer.valueOf(isship));
        }
        if(!shipnumber.equals("")){
            order.setShipnumber(shipnumber);
        }

        if(!ispay.equals("")){
            order.setIspay(Integer.valueOf(ispay));
        }
        if(!createtime.equals("")){
            order.setCreatetime(sdf.parse(createtime));
        }
        if(!endtime.equals("")){
            order.setUpdatetime(sdf.parse(endtime));
        }
        if(!iscovert.equals("")){
            order.setIscovert(Integer.valueOf(iscovert));
        }
        if(!usernumber.equals("")){
            order.setOpenid(usernumber);
        }
        try {
            Object size = orderService.getordercount(order);
            System.out.println(size);
            List<Order> list = orderService.getlist(order, pages, limits);
            map.put("code", 0);
            map.put("msg", "chenggong");
            map.put("count", size);
            map.put("data", list);
        } catch (Exception e) {
            logger.error("查询失败");
        }
        return map;
    }

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MyAddressService myAddressService;




    @ResponseBody
    @RequestMapping("getorderdetail")
    public Map<String, Object> getorderlist(@RequestParam Integer orderid, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<OrderGoods> list = orderService.getGoodtypeByorderrid(orderid);
            List<Map<String,Object>> list2=new ArrayList<>();

            for (OrderGoods orderGoods:
                 list) {
                JSONObject js=new JSONObject();
                js.put("id",orderGoods.getId());
                js.put("addressid",orderGoods.getAddressid());
                js.put("orderid",orderGoods.getOrderid());
                js.put("goodsnum",orderGoods.getGoodsnum());
                js.put("goodsprice",orderGoods.getGoodsprice());
                Goods goods=goodsService.getGoodsType(orderGoods.getGoodsid());
                MyAddress myAddress=myAddressService.findbyid(orderGoods.getAddressid());
                js.put("goodsname",goods.getGoodsname()+"["+goods.getSpecifications()+"]");
                js.put("address",myAddress.getArea()+myAddress.getAreadetail());
                js.put("shname",myAddress.getShname());
                js.put("phone",myAddress.getPhone());
                list2.add(js);
            }
            map.put("code", 0);
            map.put("msg", "chenggong");
            map.put("count", list2.size());
            map.put("data", list2);
        } catch (Exception e) {
            map.put("code", -1);
            map.put("msg", "err");
            map.put("count", 0);
            map.put("data", new JSONArray());
            logger.error("查询失败");
        }
        return map;
    }


    @ResponseBody
    @RequestMapping("updateorder")
    public String updateorder(@RequestParam Integer id,@RequestParam String shipnumber,HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        ResponseMessage resp = new ResponseMessage();
        try {
           Order order=   orderService.findbyid(id);
           if(!order.getIspay().equals(1)){
               resp.setResultDesc("此订单未付款！");
           }
           order.setShipnumber(shipnumber);
           order.setShiptime(new Date());
           order.setIsship(1);
           boolean f=orderService.update(order);
           if(f){
               resp.succeed();
               resp.setResultDesc("录入成功");
           }
        } catch (Exception e) {
            logger.error("录入失败");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }


    @ResponseBody
    @RequestMapping("deleteorder")
    public String deleteorder(@RequestParam Integer id,HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        ResponseMessage resp = new ResponseMessage();
        try {
            Order order=   orderService.findbyid(id);
            if(order==null){
                resp.setResultDesc("此订单不存在");
            }
            order.setState(-1);
            boolean f=orderService.update(order);
            if(f){
                resp.succeed();
                resp.setResultDesc("删除成功");
            }
        } catch (Exception e) {
            logger.error("删除失败");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }


}
