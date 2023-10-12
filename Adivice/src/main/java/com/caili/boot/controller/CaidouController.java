package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.Account;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.TurnOverCaidou;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.AccountDetailService;
import com.caili.boot.service.AccountService;
import com.caili.boot.service.TurnOverCaidouService;

import com.caili.boot.util.CheckUtil;
import com.caili.boot.util.CryptUtils;


import com.caili.boot.util.MD5;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("advice")
public class CaidouController {
    static Logger logger = LoggerFactory.getLogger(CaidouController.class.getName());


    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private TurnOverCaidouService turnOverCaidouService;


    /***
     * 获取彩豆明细
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/caidou/getturnover")
    public String getlistofcaidou(HttpServletRequest request) {
        ResultMsg msg = new ResultMsg();
        try {
            Integer page = Integer.parseInt(request.getParameter("page"));
            Integer limit = Integer.parseInt(request.getParameter("limit"));
            Integer pageStart = (page - 1) * limit;
            String openid = request.getParameter("openid");
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid异常,openid=" + openid);
                return JSONObject.toJSONString(msg);
            }
            List<Map<String, String>> list = turnOverCaidouService.getcaidoumingxi(pageStart, limit, openid);
            JSONArray json = new JSONArray();
            for (Map<String, String> map :
                    list) {
                json.add(JSONObject.toJSONString(map));
            }
            msg.succeed();
            msg.setObj(JSONObject.toJSON(json));
        } catch (Exception e) {
            logger.error("查询异常");


        }

        return JSONObject.toJSONString(msg);
    }


    /***
     * 获取彩豆余额
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/caidou/getcaidounum")
    public String getcaidounum(HttpServletRequest request) {
        ResultMsg msg = new ResultMsg();
        try {
            String openid = request.getParameter("openid");
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid异常,openid=" + openid);
                return JSONObject.toJSONString(msg);
            }
            //查询用户是否注册
            Integer accounnum = accountDetailService.findAccountDetailByOpenid(openid);
            if (accounnum < 1) {
                msg.setResultCode("001");
                msg.setResultDesc("未找到该用户");
                return JSONObject.toJSONString(msg);
            }
            Map<String, String> acco = accountDetailService.findallbyopenid(openid);
            AccountDetail accountdetail = JSON.parseObject(JSON.toJSONString(acco), AccountDetail.class);

            Integer signday = accountdetail.getSignday();
            if (accountdetail.getSignday().equals(0)) { //首次签到
                Map<String, Object> map = new HashMap<>();
                map.put("issign", true);
                map.put("caidoubalance", accountdetail.getCaidoubalance());
                map.put("day", 0);
                msg.succeed();
                msg.setObj(map);
                return JSONObject.toJSONString(msg);
            }
            Long lasttime = Long.valueOf(accountdetail.getLastsigntime());
            Long nowtime = System.currentTimeMillis();
            if (CryptUtils.isSameDayOfMillis(lasttime, nowtime)) {//验证两个时间是否是同一天
                Map<String, Object> map = new HashMap<>();
                map.put("issign", false);
                map.put("caidoubalance", accountdetail.getCaidoubalance());
                map.put("day", signday);
                msg.setResultCode("000");
                msg.setResultDesc("今天已经签到了");
                msg.setObj(map);
                return JSONObject.toJSONString(msg);
            }
            //转化为日期
            lasttime = CryptUtils.getmoutime(CryptUtils.getriqi(lasttime)).getTime();
            nowtime = CryptUtils.getmoutime(CryptUtils.getriqi(nowtime)).getTime();
            //判断是否断签
            if (nowtime - lasttime > 86400000) { //断签重新开始
                msg.succeed();
                Map<String, Object> map = new HashMap<>();
                map.put("caidoubalance", accountdetail.getCaidoubalance());
                map.put("issign", true);
                map.put("day", 0);
                msg.setObj(map);

            } else {
                msg.succeed();
                Map<String, Object> map = new HashMap<>();
                map.put("caidoubalance", accountdetail.getCaidoubalance());
                map.put("issign", true);
                map.put("day", accountdetail.getSignday());
                msg.setObj(map);

            }

        } catch (Exception e) {
            logger.error("获取彩豆余额异常");


        }
        return JSONObject.toJSONString(msg);
    }

    @Autowired
    private AccountService accountService;

    /***
     * 彩豆转移
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/caidou/change")
    public String changge(HttpServletRequest request) {
        logger.info("/caidou/change");
        String key = "caidoushenghuoquan2020";
        ResultMsg msg = new ResultMsg();
        String openid="",sign="",lastGainTime="",phone="";
        Integer amount=0,signDays=0;
        Long t=null;
        try {
         openid=request.getParameter("openid");
         sign=request.getParameter("sign");
         t=Long.valueOf(request.getParameter("t"));
         lastGainTime=request.getParameter("lastGainTime");
         amount=Integer.valueOf(request.getParameter("amount"));
         signDays=Integer.valueOf(request.getParameter("signDays"));
         phone=request.getParameter("phone");
        } catch (Exception e) {
            logger.error("/caidou/change请求参数异常");
            return JSONObject.toJSONString(msg);
        }
        logger.info("openid=" + openid + "；t=" + t + "；lastGainTime=" + lastGainTime + "；amount=" + amount + "；signDays=" + signDays + "；phone=" + phone + "；sign=" + sign);
        StringBuilder sd = new StringBuilder();
        sd.append("amount=" + amount);
        sd.append("&lastGainTime=" + lastGainTime);
        sd.append("&phone=" + phone);
        sd.append("&signDays=" + signDays);
        sd.append("&t=" + t);
        sd.append("&key=" + key);
        String str = sd.toString();
        if (!MD5.encode(str).equals(sign)) {
            msg.setResultDesc("签名错误");
            return JSONObject.toJSONString(msg);
        }
        if (CheckUtil.isNullAndUndefined(openid)) {
            msg.setResultDesc("openid不能为空");
            logger.error("openid异常,openid=" + openid);
            return JSONObject.toJSONString(msg);
        }
        Integer accounnum = accountDetailService.findAccountDetailByOpenid(openid);
        try {
            if (accounnum < 1) {
                logger.info("用户不存在，去注册转移");
                AccountDetail accountDetail = new AccountDetail();
                accountDetail.setOpenid(openid);
                accountDetail.setLastsigntime(lastGainTime);
                accountDetail.setCaidoubalance(amount);
                accountDetail.setIschange(1);
                accountDetail.setSignday(signDays);
                Integer a = accountDetailService.saveAccountDetail(accountDetail);
                Account account = new Account();
                account.setOpenid(openid);
                account.setCreatetime(CryptUtils.getnowtime());
                account.setState(0);
                account.setPhone(phone);
                Integer b = accountService.Register(account);
                if (!a.equals(1) && b.equals(1)) {
                    msg.setResultDesc("注册失败");
                    logger.error("注册失败");
                    return JSONObject.toJSONString(msg);
                }
                logger.info("注册成功");
                if (!saveBill(openid, "+" + amount, "彩豆转移", amount).equals(1)) {
                    msg.setResultDesc("保存到订单失败");
                    logger.error("保存到订单失败");
                    return JSONObject.toJSONString(msg);
                }
                logger.info("转移成功");
                msg.succeed();
            } else {
                logger.info("转移用户" + openid + "彩豆");
                Map<String, String> acco = accountDetailService.findallbyopenid(openid);
                AccountDetail ass = JSON.parseObject(JSON.toJSONString(acco), AccountDetail.class);
                if(ass.getIschange().equals(1)){
                    logger.error("此用户已转移");
                    msg.setResultDesc("您已经转移过了");
                    return JSONObject.toJSONString(msg);
                }
                ass.setSignday(signDays);
                ass.setIschange(1);
                ass.setLastsigntime(lastGainTime);
                ass.setCaidoubalance(ass.getCaidoubalance() + amount);
                Integer a = accountDetailService.updateAccountDetail(ass);
                Account account = new Account();
                account.setPhone(phone);
                account.setOpenid(openid);
                Integer b = accountService.update(account);
                if (!a.equals(1) && b.equals(1)) {
                    msg.setResultDesc("修改失败");
                    logger.error("修改失败");
                    return JSONObject.toJSONString(msg);
                }
                logger.info("修改成功");

                if (!saveBill(openid, "+" + amount, "彩豆转移", ass.getCaidoubalance()).equals(1)) {
                    msg.setResultDesc("保存到订单失败");
                    logger.error("保存到订单失败");
                    return JSONObject.toJSONString(msg);
                }
                logger.info("转移成功");
                msg.succeed();

            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("转移异常");


        }


        return JSONObject.toJSONString(msg);
    }


    /**
     * 签到获取彩豆
     *
     * @param request
     * @return
     * @throws ParseException
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/caidou/signin")
    public String Signin(@RequestBody JSONObject body, HttpServletRequest request) throws ParseException {
        logger.info("进入/caidou/signin");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();

        try {
            String openid = String.valueOf(body.get("openid")); //openid
            // String openid = request.getParameter("openid");
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid异常,openid=" + openid);
                return JSONObject.toJSONString(msg);
            }
            //查询用户是否注册
            Integer accounnum = accountDetailService.findAccountDetailByOpenid(openid);
            Map<String, String> acco = accountDetailService.findallbyopenid(openid);
            AccountDetail accountdetail = JSON.parseObject(JSON.toJSONString(acco), AccountDetail.class);
            if (accountdetail.getLastsigntime() == null) { //
                logger.error("不存在此用户");
                msg.setResultDesc("该用户不存在");
                return JSONObject.toJSONString(msg);
              /*  if (FirstSaveAcc(openid, 1, 0, CheckUtil.getcaidounum(1)).equals(1)) {
                    logger.info("用户保存成功");
                    if (saveBill(openid, "+10", "签到1天", 10).equals(1)) {
                        logger.info("保存签到订单成功");
                        msg.succeed();
                        Map<String, Object> map = new HashMap<>();
                        map.put("caidounum", 10);
                        map.put("issign", true);
                        msg.setObj(map);
                    }
                }*/
            } else {
                Integer signday = accountdetail.getSignday();
                if (accountdetail.getSignday().equals(0)) { //首次签到
                    accountdetail.setSignday(1);
                    accountdetail.setCaidoubalance(accountdetail.getCaidoubalance() + 10);
                    accountdetail.setLastsigntime(CryptUtils.getnowtime());
                    if (accountDetailService.updateAccountDetail(accountdetail) == 1) {//更新用户信息
                        if (saveBill(openid, "+10", "首次签到", accountdetail.getCaidoubalance()).equals(1)) {//保存到彩豆流水
                            logger.info("保存签到订单成功");
                            msg.succeed();
                            Map<String, Object> map = new HashMap<>();
                            map.put("caidounum", 10);
                            map.put("issign", true);
                            map.put("signday", 1);
                            msg.setObj(map);
                        }
                    }
                    return JSONObject.toJSONString(msg);
                }
                Long lasttime = Long.valueOf(accountdetail.getLastsigntime());
                Long nowtime = System.currentTimeMillis();
                if (CryptUtils.isSameDayOfMillis(lasttime, nowtime)) {//验证两个时间是否是同一天
                    Map<String, Object> map = new HashMap<>();
                    map.put("issign", false);
                    map.put("caidounum", 0);
                    map.put("signday", signday);
                    msg.setResultCode("000");
                    msg.setResultDesc("今天已经签到了");
                    msg.setObj(map);
                    return JSONObject.toJSONString(msg);
                }
                //转化为日期
                lasttime = CryptUtils.getmoutime(CryptUtils.getriqi(lasttime)).getTime();
                nowtime = CryptUtils.getmoutime(CryptUtils.getriqi(nowtime)).getTime();
                //判断是否断签
                if (nowtime - lasttime > 86400000) { //断签重新开始
                    accountdetail.setSignday(1);
                    accountdetail.setCaidoubalance(accountdetail.getCaidoubalance() + 10);
                    accountdetail.setLastsigntime(CryptUtils.getnowtime());
                    if (accountDetailService.updateAccountDetail(accountdetail) == 1) {//更新用户信息
                        if (saveBill(openid, "+10", "断签--签到1天", accountdetail.getCaidoubalance()).equals(1)) {//保存到彩豆流水
                            logger.info("保存签到订单成功");
                            msg.succeed();
                            Map<String, Object> map = new HashMap<>();
                            map.put("caidounum", 10);
                            map.put("issign", true);
                            map.put("signday", 1);
                            msg.setObj(map);
                        }
                    }
                } else {
                    if (accountdetail.getSignday().equals(7)) {//第八天重新开始
                        accountdetail.setSignday(1);
                        accountdetail.setLastsigntime(CryptUtils.getnowtime());
                        accountdetail.setCaidoubalance(accountdetail.getCaidoubalance() + CheckUtil.getcaidounum(1));
                    } else {
                        accountdetail.setSignday(accountdetail.getSignday() + 1);
                        accountdetail.setLastsigntime(CryptUtils.getnowtime());
                        accountdetail.setCaidoubalance(accountdetail.getCaidoubalance() + CheckUtil.getcaidounum(accountdetail.getSignday()));
                    }
                    if (accountDetailService.updateAccountDetail(accountdetail) == 1) {//更新用户信息
                        if (saveBill(openid, "+" + CheckUtil.getcaidounum(accountdetail.getSignday()), "签到成功", accountdetail.getCaidoubalance()).equals(1)) {//保存到彩豆流水
                            logger.info("保存签到订单成功");
                            msg.succeed();
                            Map<String, Object> map = new HashMap<>();
                            map.put("caidounum", CheckUtil.getcaidounum(accountdetail.getSignday()));
                            map.put("issign", true);
                            map.put("signday", accountdetail.getSignday());
                            msg.setObj(map);
                        }
                    }

                }


            }

        } catch (Exception e) {
            logger.error("签到出现异常，事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }
        return JSONObject.toJSONString(msg);
    }


    /**
     * @param openid
     * @param singnday  签到第几天
     * @param ischange  是否转移
     * @param caidounum 彩豆剩余
     * @return
     */
    private Integer FirstSaveAcc(String openid, Integer singnday, Integer ischange, Integer caidounum) {
        AccountDetail ac = new AccountDetail();
        ac.setOpenid(openid);
        ac.setSignday(singnday);
        ac.setIschange(ischange);
        ac.setCaidoubalance(caidounum);
        ac.setLastsigntime(CryptUtils.getnowtime());
        Integer a = accountDetailService.updateAccountDetail(ac);
        return a;
    }


    /**
     * @param openid
     * @param caidouturnover 流水
     * @param turnover       类型
     * @param nowcaidouprice 当前余额
     * @return
     */
    private Integer saveBill(String openid, String caidouturnover, String turnover, Integer nowcaidouprice) {
        TurnOverCaidou turnOverCaidou = new TurnOverCaidou();
        turnOverCaidou.setOpenid(openid);
        turnOverCaidou.setCaidouturnover(caidouturnover);
        turnOverCaidou.setTurnover(turnover);
        turnOverCaidou.setNowcaidouprice(nowcaidouprice);
        turnOverCaidou.setCreatetime(CryptUtils.getnowtime());
        Integer a = turnOverCaidouService.savebill(turnOverCaidou);
        return a;
    }


}
