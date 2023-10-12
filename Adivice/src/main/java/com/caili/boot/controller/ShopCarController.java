package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.*;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.AccountDetailService;
import com.caili.boot.service.GoodsService;
import com.caili.boot.service.OrderService;
import com.caili.boot.service.ShopCarService;
import com.caili.boot.util.CheckUtil;

import com.caili.boot.util.CryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("advice")
@SessionAttributes("shopcar")
public class ShopCarController {
    static Logger logger = LoggerFactory.getLogger(ShopCarController.class.getName());


    @Autowired
    private ShopCarService shopCarService;

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private OrderService orderService;


    static final Integer COVERTMONEY = 500;


    @Value("${linux.filepath}")
    private String filepaths;


    @ResponseBody
    @RequestMapping("/account/deletebill")
    public String deletebill(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/account/deletebill");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        try {
            String openid = String.valueOf(body.get("openid"));
            String ordernumber = String.valueOf(body.get("ordernumber"));
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid is null!");
                return JSONObject.toJSONString(msg);
            }
            Order order=new Order();
            order.setOrdernumber(ordernumber);
            order.setOpenid(openid);
            order.setUpdatetime(CryptUtils.getnowtime());
            Integer s=orderService.deleteOrder(order);
            if(!s.equals(1)){
                logger.info("没有可以删除的订单");
            }
            logger.info("删除订单成功！");
            msg.succeed();
        } catch (Exception e) {
            logger.error("订单异常");

        }
        return JSONObject.toJSONString(msg);
    }

    @Transactional
    @ResponseBody
    @RequestMapping("/account/getbill")
    public String getbill(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/account/getbill");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
       try {

            String openid = String.valueOf(body.get("openid"));
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid is null!");
                return JSONObject.toJSONString(msg);
            }
            List<Order> list = orderService.getorderbyopenid(openid);

            JSONArray js2=new JSONArray();
            for (Order order:
                 list) {
                JSONObject js=new JSONObject();
                js.put("orderid",order.getId());//订单id
                js.put("ordernumber",order.getOrdernumber());//订单号
                js.put("createtime",order.getCreatetime());//订单创建时间
                js.put("shipnumber",order.getShipnumber());//快递单号
                js.put("ispay",order.getIspay());//是否支付
                js.put("isreceipts",order.getIsreceipts());//是否收获
                js.put("isship",order.getIsship());//是否发货
                js.put("shiptime",order.getIsship()==0?"未发货":order.getShiptime());//发货时间
                js.put("covert_money",order.getCovert_money());//折扣金额
                js.put("payrmb",order.getPayprice());//支付金额
                js.put("phone",order.getOrderGoods().get(0).getMyAddress().getPhone());//收货人手机号码
                js.put("area", order.getOrderGoods().get(0).getMyAddress().getArea());//收货人地址
                js.put("areadetail",order.getOrderGoods().get(0).getMyAddress().getAreadetail());//收货人详细地址
                js.put("addressbyname",order.getOrderGoods().get(0).getMyAddress().getShname());//收货人姓名
                js.put("addressid",order.getOrderGoods().get(0).getMyAddress().getId());//地址id
                js.put("prepay_id",order.getPrepay_id());//微信支付
                JSONArray js3=new JSONArray();
                for (OrderGoods orderGoods:
                     order.getOrderGoods()) {
                    JSONObject jsgoods=new JSONObject();
                    jsgoods.put("goodsimg","http://www.gzclkeji.com/caidouShop/image/"+orderGoods.getGoods().getTitleimage());//商品图片
                    jsgoods.put("goodsname",orderGoods.getGoods().getGoodsname());//商品名
                    jsgoods.put("goodnum",orderGoods.getGoodsnum());//商品数量
                    jsgoods.put("goodsprice",orderGoods.getGoods().getFavorableprice()/100);//商品价格
                    jsgoods.put("goodsid",orderGoods.getGoodsid());//商品id
                    jsgoods.put("ordergoodsid",orderGoods.getId());//商品订单id

                    js3.add(jsgoods);


                }
                js.put("ordergoosobj",js3);
                js2.add(js);

            }
         //   net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(list2);
            msg.succeed();
            msg.setObj(js2);
        //    msg.setObj(json);

      } catch (Exception e) {
            logger.error("获取我的订单失败");

        }

        return JSONObject.toJSONString(msg);
    }

    @Transactional
    @ResponseBody
    @RequestMapping("/goods/saveshopcar")
    public String saveshopcar(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/goods/saveshopcar");
        logger.info(JSON.toJSONString(body));
        HttpSession session = request.getSession();
        session.removeAttribute("shopcar");
        logger.info("clear session key= shopcar");
        ResultMsg msg = new ResultMsg();
        try {
            String openid = "";
            Integer goodsid = 0, num = 0, state = 0;
            try {
                openid = String.valueOf(body.get("openid"));
                goodsid = Integer.valueOf(String.valueOf(body.get("goodsid")));
                num = Integer.valueOf(String.valueOf(body.get("num")));
                state = Integer.valueOf(String.valueOf(body.get("state")));
            } catch (Exception e) {
                logger.error("参数输入异常");
            }
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid is null!");
                return JSONObject.toJSONString(msg);
            }
            if(shopCarService.getCountByopenidandgoodsid(openid,String.valueOf(goodsid))>=1){
                List<Map<String,String>> ls=shopCarService.getShopCarbyopenidandorderid(openid,String.valueOf(goodsid));
                ShopCar shopCaru = JSON.parseObject(JSON.toJSONString(ls.get(0)), ShopCar.class);
                shopCaru.setNum(shopCaru.getNum()+num);
                shopCaru.setCreatetime(CryptUtils.getnowtime());
                shopCaru.setUpdatetime(CryptUtils.getnowtime());
                Integer vluez =   shopCarService.updateshopcar(shopCaru);
                if(vluez==1){
                    msg.succeed();
                    logger.error("保存成功");
                }
            }else{
                ShopCar shopCar = new ShopCar();
                shopCar.setOpenid(openid);
                shopCar.setNum(num);
                shopCar.setState(state);
                shopCar.setGoodsid(goodsid);
                shopCar.setCreatetime(CryptUtils.getnowtime());
                shopCar.setUpdatetime(CryptUtils.getnowtime());

                Integer vlue = shopCarService.saveMyshopcar(shopCar);
                if (vlue == 0) {
                    logger.error("save shopcar faile");
                }
                msg.succeed();
            }


        } catch (Exception e) {
            logger.error("购物车添加商品失败");

        }
        return JSONObject.toJSONString(msg);
    }


    @Transactional
    @ResponseBody
    @RequestMapping("/goods/updateshopcar")
    public String updateshopcar(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/goods/updateshopcar");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        HttpSession session = request.getSession();
        session.removeAttribute("shopcar");
        logger.info("clear session key= shopcar");
        try {

            Integer id = 0, num = 0, state = 0;
            try {
                id = Integer.valueOf(String.valueOf(body.get("id")));
                num = Integer.valueOf(String.valueOf(body.get("num")));
                state = Integer.valueOf(String.valueOf(body.get("state")));
            } catch (Exception e) {
                logger.error("参数输入异常");

            }
            if (id == 0) {
                logger.error("id is null!");
                return JSONObject.toJSONString(msg);
            }
            ShopCar shopCar = new ShopCar();
            shopCar.setId(id);
            shopCar.setNum(num);
            shopCar.setState(state);
            shopCar.setUpdatetime(CryptUtils.getnowtime());
            Integer vlue = shopCarService.updateshopcar(shopCar);
            if (vlue == 0) {
                logger.error("update shopcar faile");
            }
            msg.succeed();

        } catch (Exception e) {
            logger.error("购物车添加商品失败");

        }
        return JSONObject.toJSONString(msg);
    }


    /****
     * 查询我的购物车
     * @param body
     * @param request
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/goods/getshopcarlist")
    public String getgoods(@RequestBody JSONObject body, HttpServletRequest request) {
        ResultMsg msg = new ResultMsg();
        HttpSession session = request.getSession();

        try {
            //if (session.getAttribute("shopcar") != null) {
         //       logger.info("get session param seccess! key=shopcar");
    //            msg.succeed();
     //           msg.setObj(session.getAttribute("shopcar"));
     //           return JSONObject.toJSONString(msg);
      //      }

            String openid = String.valueOf(body.get("openid")); //openid
            Integer iscover = Integer.valueOf(String.valueOf(body.get("iscover"))); //是否使用积分兑换

            Integer goods_price = 0;  //总价格
            Integer convert_caidou = 0; //折扣多少彩豆
            Integer convert_price = 0; //抵扣多少钱
            Integer freight = 0; //运费
            Integer last_price = 0; //最终价格

            JSONObject json = new JSONObject();//返回obj

            JSONArray arr = new JSONArray();//存入商品

            List<ShopCar> arrcount = new ArrayList<>();
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid is null!");
                return JSONObject.toJSONString(msg);
            }
            Map<String, String> acco = accountDetailService.findallbyopenid(openid); //查询我的彩豆
            AccountDetail accountdetail = JSON.parseObject(JSON.toJSONString(acco), AccountDetail.class);


            Integer count = shopCarService.getCountByopenid(openid);//查询购物车商品的数量
            if (count == 0) {//购物车数量为空
                logger.info("shopcar num is null!");
                json.put("shopcar", arr);
                json.put("goods_price", 0);
                json.put("iscover", 0);
                json.put("convert_caidou", 0);
                json.put("convert_price", 0);
                json.put("freight", 0);
                json.put("last_price", 0);
                json.put("mycadidou_balance", accountdetail.getCaidoubalance());
                msg.succeed();
                msg.setResultDesc("购物车没有添加商品");
                msg.setObj(json);
                return JSONObject.toJSONString(msg);
            }
            List<ShopCar> list = shopCarService.getShopCarbyopenid(openid);//查询购物车数据


            for (ShopCar shopcars :
                    list) {
                if (shopcars.getState().equals(1)) {
                    arrcount.add(shopcars);  //添加选中状态的对象到数组
                }
                String goodsname = shopcars.getGoods().getGoodsname();
                Integer favorableprice = (shopcars.getGoods().getFavorableprice()) / 100;
                String goodsimg = CheckUtil.getUrlimg(shopcars.getGoods().getTitleimage().split(","), filepaths).get(0);
                //shopcars.setGoods(null);
                JSONObject obj = (JSONObject) JSONObject.toJSON(shopcars);
                obj.remove("goods");
                obj.put("goodsname", goodsname);
                obj.put("favorableprice", favorableprice);
                obj.put("goodsimg", goodsimg);
                arr.add(obj);
            }


            Integer maxCover1 = accountdetail.getCaidoubalance() / 5000; //获取我的彩豆可以折扣的次数
            Integer maxCover2 = 0;
            for (ShopCar shopCar : arrcount) {
                Integer jiage = shopCar.getGoods().getFavorableprice();//价格
                Integer shuliang = shopCar.getNum();//数量
                goods_price = goods_price + jiage * shuliang;//总价格
                maxCover2 = maxCover2 + shopCar.getNum();
            }

            Integer maxCovertnum = getMaxCover(maxCover1, maxCover2);//最大折扣次数
            logger.info("最大折扣次数" + maxCovertnum);
            if (iscover.equals(1)) {//选中折扣  但不能折扣
                if (maxCovertnum.equals(0)) {
                    iscover = 0;
                    logger.info("不能进行折扣！");
                }
            }
            if (iscover.equals(0)) {//能折扣  没有选中折扣
                maxCovertnum = 0;
            }

            convert_caidou = maxCovertnum * 5000; //折扣的彩豆
            convert_price = maxCovertnum * COVERTMONEY; ///折扣的金额

            last_price = goods_price - convert_price;//最后的金额

            json.put("shopcar", arr);
            json.put("goods_price", goods_price / 100);
            json.put("iscover", iscover);
            json.put("convert_caidou", convert_caidou);
            json.put("convert_price", convert_price / 100);
            json.put("freight", 0);
            json.put("last_price", last_price / 100);
            json.put("mycadidou_balance", accountdetail.getCaidoubalance());
            logger.info("save shopcar in session");
            session.setAttribute("shopcar", json);
            msg.succeed();
            msg.setObj(json);
        } catch (Exception e) {
            logger.error("拉取购物车信息异常");

        }
        return JSONObject.toJSONString(msg);
    }


    /**
     * 得到最小的
     *
     * @return
     */
    private Integer getMaxCover(Integer a, Integer b) {
        return a <= b ? a : b;
    }


}
