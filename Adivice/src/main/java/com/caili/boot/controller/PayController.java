package com.caili.boot.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.*;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.*;
import com.caili.boot.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
@Controller
@RequestMapping("advice")
public class PayController {
    static Logger logger = LoggerFactory.getLogger(PayController.class.getName());
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private TurnOverCaidouService turnOverCaidouService;

    @Autowired
    private ShopCarService shopCarService;

    @Value("${weixin.appId}")
    private String APPID;

    @Value("${weixin.myshopnumber}")
    private String MCHID;

    public String xaidan(@RequestBody JSONObject body) {
        //  WechatUtil.wxPay();创建订单

        return super.toString();
    }


    @Transactional
    @ResponseBody
    @RequestMapping("/jsPayResult")
    public String jsapiresult(HttpServletRequest request) throws Exception {
        logger.info("进入jsPayResult");
      //  logger.info(JSON.toJSONString(request));
        ResultMsg msg = new ResultMsg();
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        Map<String, String> map;
        String code = WechatUtil.getRandomStr(6);

            map = MessageUtil.parseXml(request);
            logger.info("【支付通知" + code + "】请求进入，ip为:" + request.getRemoteAddr() + "，参数为：" + JSONObject.toJSONString(map));
            if (map.containsKey("return_code") && "SUCCESS".equals(map.get("return_code"))) {
                if (map.containsKey("sign")) {
                    if (!WechatUtil.checkParam(map)) {
                        //result = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml>";
                        logger.error("【支付通知" + code + "】验证签名失败");
                    } else {
                        if (map.containsKey("result_code") && "SUCCESS".equals(map.get("result_code"))) {
                            Order order2 = orderService.getGoodsbyordernumber(map.get("out_trade_no"));
                            Map<String, String> acs = accountDetailService.findallbyopenid(map.get("openid"));
                            AccountDetail ac = JSON.parseObject(JSON.toJSONString(acs), AccountDetail.class);
                            try {
                                Integer res = orderService.updateOrder(map);
                                logger.info("获取我的订单"+res);
                                logger.info(order2.toString());
                                String openid = order2.getOpenid();
                                Integer iscovert = order2.getIscovert();
                                Integer caidokc = order2.getCovert_caidou();
                                if (iscovert == 1) {
                                    logger.info("此订单需要扣除彩豆" + caidokc);

                                    AccountDetail ac2 = new AccountDetail();
                                    ac2.setCaidoubalance(ac.getCaidoubalance() - caidokc);
                                    ac2.setOpenid(ac.getOpenid());
                                    ac2.setIschange(ac.getIschange());
                                    Integer res2 = accountDetailService.updateAccountDetail(ac2);
                                    if (res2.equals(1)) {
                                        logger.info(openid + "成功扣除" + caidokc + "彩豆");
                                        TurnOverCaidou turnOverCaidou = new TurnOverCaidou();
                                        turnOverCaidou.setOpenid(openid);
                                        turnOverCaidou.setCaidouturnover("-" + caidokc);
                                        turnOverCaidou.setTurnover("购买商品扣除");
                                        turnOverCaidou.setNowcaidouprice(ac.getCaidoubalance() - caidokc);
                                        turnOverCaidou.setCreatetime(CryptUtils.getnowtime());
                                        Integer a = turnOverCaidouService.savebill(turnOverCaidou);
                                        if (a.equals(1)) {
                                            logger.info("添加到彩豆流水成功！");
                                            return "success";
                                        }

                                    } else {
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                        logger.error(openid + "扣除彩豆失败");
                                    }
                                } else {
                                    logger.info("订单生成成功");
                                    return "success";
                                }
                             } catch (Exception e) {
                                logger.error("创建订单异常，事务回滚");
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


                            }








                       /*     } else {
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                logger.error("【支付通知" + code + "】此订单已经支付" + map.get("out_trade_no"));
                            }*/
                            // orderInfoService.updSuccessStatus(map);
                        } else {
                            //result = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[状态码异常]]></return_msg></xml>";
                            logger.error("【支付通知" + code + "】返回状态码status不正常");
                        }
                    }
                } else {
                    logger.error("【支付通知" + code + "】验证签名失败:" + new String(result.getBytes(), "UTF-8"));
                }
            } else {
                logger.error("【支付通知" + code + "】返回失败:" + new String(result.getBytes(), "UTF-8"));
            }



        return JSONObject.toJSONString(msg);
    }

    /****
     * 获取支付签名
     * @param body
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/pay/getsign")
    public String getopendid(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/pay/getsign");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        try {
            String openid = String.valueOf(body.get("openid"));
            String ordernumber = String.valueOf(body.get("prepayid"));

            if (CheckUtil.isNullAndUndefined(openid) && ordernumber == null) {
                logger.error("openid异常,openid=" + openid);
                return JSONObject.toJSONString(msg);
            }
          //  Order orders = orderService.getGoodsbyordernumber(ordernumber);
          //    if (orders == null) {
          //       logger.error("没有此订单");
          //      return JSONObject.toJSONString(msg);
          //   }
            //获取签名
            String appId = APPID;
            String timeStamp = "" + System.currentTimeMillis() / 1000;
            String nonceStr = UUID.randomUUID().toString().replace("-", "");
            String packages = "prepay_id=" + ordernumber;
            String signType = "MD5";

            Map<String, String> params = new HashMap<String, String>();
            params.put("appId", appId);
            params.put("timeStamp", timeStamp);
            params.put("nonceStr", nonceStr);
            params.put("package", packages);
            params.put("signType", signType);
            Map<String, String> sortParams = sortAsc(params);
            String str = WechatUtil.mapJoin(sortParams, false);
            params.put("paySign", MD5.encode(str + "&key=lottery2004lottery2004lottery200").toUpperCase());
            params.put("nowtime",CryptUtils.getnowtime());
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(params);
            JSONObject js = new JSONObject();
           // js.put("id", orders.getId());
            jsonObject.put("order", js);
            msg.succeed();
            msg.setObj(jsonObject);
        } catch (Exception e) {
            logger.error("获取支付参数异常");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }
        return JSONObject.toJSONString(msg);


    }


    /***
     *创建订单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/bill/create")
    public String create(@RequestBody JSONObject body, HttpServletRequest request) throws Exception {
        logger.info("/bill/create");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
     try {
        String openid = String.valueOf(body.get("openid"));
        if (CheckUtil.isNullAndUndefined(openid)) {
            logger.error("openid异常,openid=" + openid);
            return JSONObject.toJSONString(msg);
        }
        Integer paymoney = Integer.valueOf(String.valueOf(body.get("paymoney")));//支付金额
        Integer iscovert = Integer.valueOf(String.valueOf(body.get("iscovert")));//是否折扣
        Integer covertmoney = Integer.valueOf(String.valueOf(body.get("covertmoney")));//折扣金额
        Integer myaddressid = Integer.valueOf(String.valueOf(body.get("addressid")));//地址
        String goods = String.valueOf(body.get("goods"));//商品参数
        List<JSONObject> js = chekparam(paymoney, iscovert, covertmoney, goods);
        // System.out.println(js.toString());
        if (!js.get((js.size() - 1)).get("code").equals("1")) {
            logger.error("请求数据格式错误！");
            return JSONObject.toJSONString(msg);
        }
        //批量清空购物车
        List<ShopCar> as = new ArrayList<>();
        String []arr  = String.valueOf(body.get("shopcarid")).split("-");
        Integer fld=0;
        if(arr.length>=1){
            for (int i = 0; i <arr.length ; i++) {
                if(Integer.valueOf(arr[i])==0){
                    break;
                }
                ShopCar shopCarss=new ShopCar();
                shopCarss.setId(Integer.valueOf(arr[i]));
                 fld =fld+ shopCarService.deleteShopCar(shopCarss);
            }
        }
              HttpSession session = request.getSession();
              session.removeAttribute("shopcar");
              logger.info("清空session key=shopcar");
            logger.info("清空购物车成功");



        String ordernumber = getOrderIdByTime();

        Order order = new Order();
        order.setOrdernumber(ordernumber);
        order.setOpenid(openid);
        order.setPayprice(Double.valueOf(paymoney));
        order.setIspay(0);//未支付
        order.setIsship(0);
        order.setIsreceipts(0);
        order.setState(1);
        order.setIscovert(iscovert);
        if (iscovert == 0) {
            order.setCovert_caidou(0);
            order.setCovert_money(0);
        } else {
            order.setCovert_caidou(covertmoney * 1000);
            order.setCovert_money(covertmoney);
        }
        order.setUpdatetime(CryptUtils.getnowtime());
        order.setCreatetime(CryptUtils.getnowtime());
        Integer flag2 = orderService.saveOrder(order);
        System.out.println(flag2);

        if (!flag2.equals(1)) {
            logger.error("保存订单表失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JSONObject.toJSONString(msg);
        }

        Order orders = orderService.getGoodsbyordernumber(ordernumber);
        Integer orderid = orders.getId();

        List<OrderGoods> list = new ArrayList<>();
        for (int i = 0; i < js.size() - 1; i++) {
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setGoodsid(Integer.valueOf(String.valueOf(js.get(i).get("goodsid"))));
            orderGoods.setGoodsnum(Integer.valueOf(String.valueOf(js.get(i).get("goodsnum"))));
            orderGoods.setGoodsprice(Integer.valueOf(String.valueOf(js.get(i).get("goodsmoney"))));
            orderGoods.setOrderid(orderid);
            orderGoods.setState(1);
            orderGoods.setCreatetime(CryptUtils.getnowtime());
            orderGoods.setUpdatetime(CryptUtils.getnowtime());
            orderGoods.setAddressid(myaddressid);
            list.add(orderGoods);
        }
        Integer flag = orderGoodsService.saveOrderGoods(list);
        System.out.println(flag);
        if (!flag.equals(list.size())) {
            logger.error("添加商品订单表失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JSONObject.toJSONString(msg);
        }
              order.setId(orders.getId());
              net.sf.json.JSONObject orders1 = net.sf.json.JSONObject.fromObject(order);
              orders1.put("createbill", true);

        String result = WechatUtil.wxPay(APPID, MCHID, ordernumber, paymoney + "00", openid, request.getRemoteAddr(), "JSAPI", "彩豆生活圈");
        logger.info("【微信公众号支付-统一下单】返回结果为：" + result);
        if (result != null && !"".equals(result)) {
            Map<String, String> resultMap = XmlUtil.toMap(result.getBytes(), "utf-8");
            if (resultMap.containsKey("sign")) {
                if (!WechatUtil.checkParam(resultMap)) {
                    msg.setResultDesc("网络异常，请稍后重试");
                    logger.error("【微信公众号支付-统一下单】验证签名不通过");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException("【微信公众号支付-统一下单】验证签名不通过");
                } else {
                    if (resultMap.containsKey("result_code") && "SUCCESS".equals(resultMap.get("result_code"))) {
                        msg.succeed();
                        Order prepay=new Order();
                        prepay.setOrdernumber(ordernumber);
                        prepay.setPrepay_id(resultMap.get("prepay_id"));
                        prepay.setIspay(0);
                        prepay.setUpdatetime(CryptUtils.getnowtime());
                        if(!orderService.updateOrder2(prepay).equals(1)){
                            logger.error("下单失败");
                        }
                        logger.info("下单成功");
                        JSONObject jss=new JSONObject();
                        jss.put("createbill", true);
                        jss.put("prepay_id",resultMap.get("prepay_id"));
                        msg.setObj(jss);

                    } else {
                        msg.setResultDesc(resultMap.get("result_code"));
                        logger.error("【微信公众号支付-统一下单】返回状态码status不正常");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new RuntimeException("【微信公众号支付-统一下单】返回状态码status不正常");
                    }
                }
            } else {
                logger.error("【微信公众号支付-统一下单】验证签名失败:" + new String(result.getBytes(), "UTF-8"));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("【微信公众号支付-统一下单】验证签名失败:" + new String(result.getBytes(), "UTF-8"));
            }

        } else {
            msg.setResultDesc("网络异常，请稍后重试");
            logger.error("【微信公众号支付-统一下单】请求返回结果为空");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("【微信公众号支付-统一下单】请求返回结果为空");
        }


  } catch (Exception e) {
            logger.error("创建订单异常，事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }
        return JSONObject.toJSONString(msg);
    }


    public static String getOrderIdByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

    /**
     * @param paymoney
     * @param iscovert
     * @param covertmoney
     * @param goods
     * @return
     */
    private List<JSONObject> chekparam(Integer paymoney, Integer iscovert, Integer covertmoney, String goods) {

        List<JSONObject> list = new ArrayList<>();
        JSONObject json2 = new JSONObject();
        json2.put("code", "-1");
        try {
            String[] arr = goods.split(",");
            Integer lastprice = 0;
            for (int i = 0; i < arr.length; i++) {
                JSONObject json = new JSONObject();
                String[] arr2 = arr[i].split("-");
                System.out.println(arr2.length);
                if (arr2.length != 3) {
                    list.add(json2);
                    break;
                }
                Integer goodsid = Integer.valueOf(arr2[0]);
                Integer goodsmoney = Integer.valueOf(arr2[1]);
                Integer goodsnum = Integer.valueOf(arr2[2]);
                lastprice = lastprice + goodsmoney * goodsnum;
                json.put("goodsid", goodsid);
                json.put("goodsmoney", goodsmoney);
                json.put("goodsnum", goodsnum);
                list.add(json);
            }
            if (iscovert == 1) {
                paymoney = paymoney + covertmoney;
            }
            if (!paymoney.equals(lastprice)) {
                list.add(json2);
                return list;
            }

            json2.put("code", "1");
            list.add(json2);


        } catch (Exception e) {
            logger.error("参数异常");
        }

        return list;
    }


    private HashMap<String, String> sortAsc(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }


}
