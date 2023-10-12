package com.caili.boot.mains;

import com.caili.boot.entity.wechat.WechatBotton;

import com.caili.boot.util.WechatUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 创建公众号菜单
 */
@SpringBootTest
public class CreateMenu {
    private static Logger log = LoggerFactory.getLogger(CreateMenu.class);

    static String address="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    static String appid="wx96b6f3e29dd7b46e";


    static String redirect_uri="http://www.gzclkeji.com/advice/wechat/toindex";


    static  String scop="snsapi_userinfo";
    @Test
    public void Createmenu() throws JsonProcessingException, JSONException, UnsupportedEncodingException {
        String at ="40_zdaBO8xjo4swC5QNjKwA94q4EkpY-o7LY1UHPWoeFD-v8ol3R4m9u4QNIGvh8zLiLADdNOw50p54NqsxIJ3UUew8i_Fv3HVyUUUs3K8I9JZWtcWmshfEGCoZtPi5pHF6BB9X2NaKTbnCUJFrGVZiACARGK";
        String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        System.err.println(address);
        if (null != at) {
            // 调用接口创建菜单
            int result = WechatUtil.createMenu(getMenu(), at,url);

            // 判断菜单创建结果
            if (0 == result)
                log.info("菜单创建成功！");
            else
                log.info("菜单创建失败，错误码：" + result);
        }
    }
    private String getMenu() throws JsonProcessingException, JSONException, UnsupportedEncodingException {
        address =address.replace("APPID",appid).replace("REDIRECT_URI",  URLEncoder.encode(redirect_uri,"UTF-8")).replace("SCOPE",scop);
        JSONObject json=new JSONObject();
        WechatBotton btn1=new WechatBotton();
        btn1.setName("彩豆商城");
        btn1.setType("view");
        btn1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx97fc97c32c6d5c69&redirect_uri=https://info.dsgc.atianqi.com/tz_cdindex.html?ch=1&response_type=code&scope=snsapi_base&state=12369");

        WechatBotton btn2=new WechatBotton();
        btn2.setName("玩转彩豆");
        btn2.setType("view");
        btn2.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx96b6f3e29dd7b46e&redirect_uri=http://www.gzclkeji.com/caidouShop/cdindex.html?ch=1&response_type=code&scope=snsapi_base&state=12369");

        WechatBotton btn3=new WechatBotton();
        btn3.setName("我的订单");
        btn3.setType("view");
        btn3.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx96b6f3e29dd7b46e&redirect_uri=http://www.gzclkeji.com/caidouShop/user_order.html?ch=1&response_type=code&scope=snsapi_base&state=12369");

        ObjectMapper mapper = new ObjectMapper();
        JSONObject  jsonStr =new JSONObject(mapper.writeValueAsString(btn1));
        JSONObject  jsonStr2 = new JSONObject(mapper.writeValueAsString(btn2));
        JSONObject  jsonStr3 = new JSONObject(mapper.writeValueAsString(btn3));
        JSONArray arr=new JSONArray();
        arr.put(jsonStr);
        arr.put(jsonStr2);
        arr.put(jsonStr3);
        json.put("button",arr);

        System.out.println(json.toString());
         return json.toString();
    }

    @Test
    public void testss() throws ParseException {

    }

}
