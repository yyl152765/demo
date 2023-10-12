package com.caili.boot.wechat;


import com.alibaba.fastjson.JSON;
import com.caili.boot.config.Systems;
import com.caili.boot.entity.Covert;
import com.caili.boot.service.CovertService;
import com.caili.boot.util.weixinCommenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时获取AccessToken
 */
@Component
public class WeixinAccessTokenTask {
    static Logger logger = LoggerFactory.getLogger(WeixinAccessTokenTask.class);


    @Resource
    private CovertService covertService;

    private static WeixinAccessTokenTask weixinAccessTokenTask;


    public void  init(){
        weixinAccessTokenTask  =this;
        weixinAccessTokenTask.covertService=this.covertService;
    }


    @Value("${weixin.appId}")
    private String APPID;

    @Value("${weixin.appsecret}")
    private String APPSECRET;

    // 获取access_token的接口地址（GET） 限2000（次/天）
    @Value("${weixin.getAccessTockenUrls}")
    private String URL;

    // 获取access_token的接口地址（GET） 限2000（次/天）
    @Value("${environment.type}")
    private String TYPE;

    // 第一次延迟1秒执行，当执行完后7100秒再执行
    @Scheduled(initialDelay = 1000, fixedDelay = 7000*1000 )
    public void getWeixinAccessToken() throws InterruptedException {
        if(TYPE.equals("prod")){
            return;
        }else{
            try {
                String token = new weixinCommenUtil().getToken(APPID, APPSECRET,URL).getAccess_token();

                // WeixinAccessToken.setToken(token);
                logger.info("获取到的微信accessToken为"+token);
                // HttpSession session = request.getSession();
                // session.setAttribute("access_token",token);
            } catch (Exception e) {
                logger.error("获取微信adcessToken出错，信息如下");
                e.printStackTrace();
                Thread.sleep(1000*7);
                this.getWeixinAccessToken();
                // 此处可能陷入死循环
            }
        }


    }


    @Scheduled(cron = "0 0 0 * * *")
     public void clearDataJob(){
            logger.info("---------定时任务开始执行---------"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
            logger.info("更新库存1元彩金、3元彩金、5元彩金库存");
            Systems.KUCU1=30;
            Systems.KUCU3=10;
            Systems.KUCU5=6;
            logger.info("---------定时任务执行成功---------"+new  SimpleDateFormat("HH:mm:ss").format(new Date()));
        }


/*    @Scheduled(cron = "0/60 * * * * ?")
    public void changenum(){
        List<Map<String,String>> lst=weixinAccessTokenTask.covertService.getlist();
        for (Map<String,String> map:
                lst) {
            Covert covert = JSON.parseObject(JSON.toJSONString(map), Covert.class);
            logger.info("获取库存中..");
            if(covert.getType().equals(1)){
                Systems.KUCU1=covert.getNum();
            }
            if(covert.getType().equals(2)){
                Systems.KUCU3=covert.getNum();
            }
            if(covert.getType().equals(3)){
                Systems.KUCU5=covert.getNum();
            }
            logger.info("库存获取成功 一元彩金还剩"+Systems.KUCU1+"个,三元彩金还剩"+Systems.KUCU3+"个,五元彩金还剩"+Systems.KUCU5+"个");

        }


    }*/
}
