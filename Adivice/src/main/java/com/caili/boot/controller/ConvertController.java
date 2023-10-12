package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.TurnOverCaidou;
import com.caili.boot.entity.Voucher;
import com.caili.boot.entity.VoucherBill;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.AccountDetailService;
import com.caili.boot.service.TurnOverCaidouService;
import com.caili.boot.service.VoucherBillService;
import com.caili.boot.service.VoucherService;
import com.caili.boot.util.CheckUtil;
import com.caili.boot.util.CryptUtils;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("advice")
public class ConvertController {

    static Logger logger = LoggerFactory.getLogger(ConvertController.class.getName());

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private TurnOverCaidouService turnOverCaidouService;

    @Autowired
    private VoucherService  voucherService;

    @Autowired
    private VoucherBillService voucherBillService;


    /****
     * 获取我的兑换记录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/caidou/getmycoverdetail")
    public String getlistofcaidou(HttpServletRequest request) {
        logger.info("进入/caidou/getmycoverdetail");
        ResultMsg msg = new ResultMsg();

        try {
            String openid = request.getParameter("openid");
        if (CheckUtil.isNullAndUndefined(openid)) {
            logger.error("openid异常,openid=" + openid);
            return JSONObject.toJSONString(msg);
        }
        Integer count=voucherBillService.findcountbyopenid(openid);
            JSONArray json = new JSONArray();
        if(count<1){
            msg.succeed();
            msg.setObj(json);
            return JSONObject.toJSONString(msg);
        }
        List<Map<String,String>> list=voucherBillService.findvoucherbillbByopenid(openid);

            for (Map<String, String> maps :
                    list) {
                json.add(JSONObject.toJSONString(maps));
            }
            msg.succeed();
            msg.setObj(JSONObject.toJSON(json));

        } catch (Exception e) {
            logger.error("获取我的兑换记录异常");

        }

        return JSONObject.toJSONString(msg);
    }




    /**
     * 彩豆兑换彩金
     * @param request
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/caidou/caidouconvert")
    public String caidouconvert(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/caidou/caidouconvert");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();

       // String openid = request.getParameter("openid");
      //  Integer type = Integer.valueOf(request.getParameter("type"));
    try {
        String openid=String.valueOf(body.get("openid")); //openid
        Integer type=Integer.valueOf(String.valueOf(body.get("type"))); //openid
        if (CheckUtil.isNullAndUndefined(openid)) {
            logger.error("openid异常,openid=" + openid);
            return JSONObject.toJSONString(msg);
        }
        Map<String, String> acco = accountDetailService.findallbyopenid(openid);
        AccountDetail accountdetail = JSON.parseObject(JSON.toJSONString(acco), AccountDetail.class);
        if(accountdetail.getCaidoubalance()<CheckUtil.getcaijinrule(type)){
            msg.setResultCode("001");
            msg.setResultDesc("彩豆余额不足"+CheckUtil.getcaijinrule(type));
            logger.error("彩豆余额不足");
            return JSONObject.toJSONString(msg);
        }
        //扣彩豆
        accountdetail.setLastsigntime(CryptUtils.gettimebytimes(accountdetail.getLastsigntime()));
        accountdetail.setCaidoubalance(accountdetail.getCaidoubalance()-CheckUtil.getcaijinrule(type));
        Integer flag=accountDetailService.updateAccountDetail(accountdetail);
        if(!flag.equals(1)){
            return JSONObject.toJSONString(msg);
        }
        logger.info("扣除彩豆成功");
        TurnOverCaidou turnOverCaidou=new TurnOverCaidou();
        turnOverCaidou.setOpenid(openid);
        turnOverCaidou.setCreatetime(CryptUtils.getnowtime());
        turnOverCaidou.setTurnover("兑换彩金"+CheckUtil.getcdruledetail(type));
        turnOverCaidou.setCaidouturnover("-"+CheckUtil.getcaijinrule(type));
        turnOverCaidou.setNowcaidouprice(accountdetail.getCaidoubalance());
        Integer flags=    turnOverCaidouService.savebill(turnOverCaidou);
        if(!flags.equals(1)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JSONObject.toJSONString(msg);
        }
        logger.info("写入彩豆流水成功");

        //下发兑换券
        List<Map<String, String>> list = voucherService.getVoucher(1, 1, type);
        if(list.size()<1){
            msg.setResultCode("002");
            msg.setResultDesc("兑换券不足");
            logger.error("兑换券不足");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JSONObject.toJSONString(msg);
        }

        Voucher voucher = JSON.parseObject(JSON.toJSONString(list.get(0)), Voucher.class);
        voucher.setState(1);
        voucher.setCreatetime(CryptUtils.gettimebytimes(voucher.getCreatetime()));
        Integer flag2=    voucherService.Updatestate(voucher);
        if(!flag2.equals(1)){
            logger.error("下发兑换券失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JSONObject.toJSONString(msg);
        }
        logger.info("下发兑换券成功");
        String ticket=voucher.getVoucherid();

        VoucherBill bill=new VoucherBill();
        bill.setOpenid(openid);
        bill.setTypeid(type);
        bill.setTypename("兑换彩金"+CheckUtil.getcdruledetail(type));
        bill.setVoucherid(ticket);
        bill.setCreatetime(CryptUtils.getnowtime());
        Integer flag3=voucherBillService.saveVoucherBill(bill);
        if(!flag3.equals(1)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JSONObject.toJSONString(msg);
        }
        logger.info("写入我的兑换记录成功");
        msg.succeed();
        Map<String,String>  map=new HashMap<>();
        map.put("ticket",ticket);
       msg.setObj(map);
      } catch (Exception e) {
            logger.error("彩金兑换出现异常，事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }

        return JSONObject.toJSONString(msg);
    }





}
