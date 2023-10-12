package com.caili.boot.util;

import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.wechat.AccessToken;

import com.caili.boot.service.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class weixinCommenUtil {
    Logger logger = LoggerFactory.getLogger(weixinCommenUtil.class.getName());

    @Autowired
    private AccessTokenService accessTokenService;


    public AccessToken getToken(String appid, String appsecret, String url){

        AccessToken  token=new AccessToken();
        //访问微信服务器的地址
        String requestUrl=url.replace("APPID", appid).replace("APPSECRET", appsecret);
        System.out.println(requestUrl);
        String str =HttpRequestUtil.get(requestUrl);
       // String str="{\"access_token\":\"38_d2_-7wG5uROd4yafb-n4uA3Gxf5yo7J6m8NhIT-UL7IVdjF2Az8Qga6bTtVKCL2PbMFztWl_NOwCsD2BiBRNxDQOw0TNIEHwAX0PkVYjhbCp3u08KdUgvD1l-GXOp0gvx1wJqrgHj_Do5dA2NNLdAIASSQ\",\"expires_in\":7200}";
        System.out.println("获取到的json格式的Token为:"+str);
        //判断json是否为空
        if (str!=null){

            try{

                //将获取的access_token放入accessToken对象中
                JSONObject json = JSONObject.parseObject(str);
                token.setAccess_token(json.getString("access_token"));
                //将获取的expires_in时间放入accessToken对象中
                token.setExpires_in(json.getInteger("expires_in"));

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String nowdae=sdf.format(new Date());
                token.setCreatetime(nowdae);
                Cookie cookie=new Cookie("token",token.getAccess_token());
                cookie.setMaxAge(60*60*2);

            }
            catch (Exception e){
                token=null;
                e.printStackTrace();
                logger.error("获取token失败");
                System.out.println("系统出错了！");
            }
        }else {
            token=null;
            // 获取token失败
            logger.error("获取token失败 errcode:{} errmsg:{}");
        }
        return token;

    }
}
