package com.caili.boot.controller;


import com.alibaba.fastjson.JSON;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.TextMessage;
import com.caili.boot.entity.WechatConfig;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.AccessTokenService;
import com.caili.boot.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Controller
@RequestMapping("advice")
public class WechatController {

    static Logger logger = LoggerFactory.getLogger(WechatController.class.getName());


    @Value("${weixin.appId}")
    private String APPID;

    @Value("${weixin.appsecret}")
    private String APPSECRET;

    @Autowired
    private AccessTokenService accessTokenService;

    /****
     * 获取jssdk参数
     * @param body
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/wechat/config")
    public String getopendid(@RequestBody com.alibaba.fastjson.JSONObject body, HttpServletRequest request) {
        logger.info("进入/wechat/config");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        String access_token = "";
        String jsapi_ticket = "";
        try {
            String urladdress = String.valueOf(body.get("url"));
            String code = String.valueOf(body.get("code"));
            List<Map<String,String>> lis=accessTokenService.getlist();
            WechatConfig ass=  JSON.parseObject(JSON.toJSONString(lis.get(0)), WechatConfig.class);
            if(new Date().getTime()<=ass.getResttime().getTime()&&new Date().getTime()>=ass.getCreatetime().getTime()){
                jsapi_ticket=ass.getJsapi_ticket();
                logger.info("jsapi_ticket没有失效 jsapi_ticket="+jsapi_ticket);
            }
            if(jsapi_ticket.equals("")||jsapi_ticket==null){
                //1.获取access_token
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
                String strgettoken = HttpRequestUtil.post(url, "");
                logger.info("获取access_token结果"+strgettoken);
                com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(strgettoken);

                if (json.size() >= 5) {
                    access_token = json.getString("access_token");
                }

                if (access_token == null || access_token.equals("")) {
                    logger.error("access_token为空");
                    return com.alibaba.fastjson.JSONObject.toJSONString(msg);
                }
                //2.获取jsapi_ticket
                String url2 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
                String strticket = HttpRequestUtil.get2(url2);
                logger.info("获取jsapi_ticket结果"+strticket);
                com.alibaba.fastjson.JSONObject json2 = com.alibaba.fastjson.JSONObject.parseObject(strticket);
                if (json2.size() >= 4) {
                    jsapi_ticket = json2.getString("jsapi_ticket");
                }
                if (jsapi_ticket == null || jsapi_ticket.equals("")) {
                    logger.error("jsapi_ticket为空");
                    return com.alibaba.fastjson.JSONObject.toJSONString(msg);
                }
                //保存在jsapi_ticket在数据库
                WechatConfig wechatConfig=new WechatConfig();
                wechatConfig.setJsapi_ticket(jsapi_ticket);
                Date now=new Date();
                Calendar  dar=Calendar.getInstance();
                dar.setTime(now);
                dar.add(java.util.Calendar.HOUR_OF_DAY, 2);
                wechatConfig.setCreatetime(now);
                wechatConfig.setResttime(dar.getTime());

                Integer is=    accessTokenService.savewhechatconfig(wechatConfig);
                if(!is.equals(1)){
                    logger.error("保存jsapi_ticket失败");
                }
                logger.info("保存jsapi_ticket成功！");
            }


            //获取签名
            String appId = APPID;
            String timeStamp = "" + System.currentTimeMillis() / 1000;
            String nonceStr = UUID.randomUUID().toString().replace("-", "");


            Map<String, String> params = new HashMap<String, String>();
            params.put("noncestr", nonceStr);
            params.put("jsapi_ticket", jsapi_ticket);
            params.put("timestamp", timeStamp);
            params.put("url",urladdress);
            Map<String, String> sortParams = sortAsc(params);
            String str = WechatUtil.mapJoin(sortParams, false);
            //返回参数
            Map<String, String> params2 = new HashMap<String, String>();
            params2.put("signature", SignUtils.sha1(str));
            params2.put("appId", APPID);
            params2.put("timestamp", timeStamp);
            params2.put("nonceStr", nonceStr);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(params2);
            msg.succeed();
            msg.setObj(jsonObject);
        } catch (Exception e) {
            logger.error("获取支付参数异常");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }
        return com.alibaba.fastjson.JSONObject.toJSONString(msg);


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


    /**
     * 验证消息是否来自微信服务器
     * signature:微信加密签名
     * timestamp:时间戳
     * nonce:随机数
     * echostr:随机字符串
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("wechat/checkToken")
    public String checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String encryptType;
        if (request.getMethod().toUpperCase().equals("GET")) {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            logger.info("微信消息进入。。");
            logger.info("微信加密签名" + signature + "时间戳" + timestamp + "随机数" + nonce + "随机字符串" + echostr);
            //验证
            String newStr = checkfromwchat(signature, timestamp, nonce);
            logger.info("newStr" + newStr);
            if (!newStr.equals(signature.toUpperCase())) {
                newStr = "";
                logger.error("消息不是来自微信服务器！");
                return null;
            } else {
                logger.info("返回echostr到微信" + signature);
                return echostr;
                //  response.getOutputStream().println(echostr);
            }
        } else {//处理微信消息
            String result;
            try {
                encryptType = request.getParameter("encrypt_type");
            } catch (Exception e) {
                encryptType = "";
            }

            if ("aes".equals(encryptType)) {
                result = processEncryptRequest(request);
            } else {
                result = processRequest(request);
            }

            return result;
        }


    }


    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    private String processRequest(HttpServletRequest request) {
        Map<String, String> requestMap = null;
        try {
            requestMap = MessageUtil.parseXml(request);
        } catch (Exception e) {
            logger.error("处理微信发送请求异常:", e);
        }

        if (null != requestMap) {
            return processRequestMap(requestMap);
        }
        return "success";
    }

    /**
     * 处理微信发来的请求（加密流程）
     *
     * @param request
     * @return
     */
    public String processEncryptRequest(HttpServletRequest request) {
        Map<String, String> requestMap = null;
        String respMessage = null;
        try {
            // 进行解密
            String msgSignature = request.getParameter("msg_signature");
            String timeStamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");

            // 微信官方提供的工具
            //   WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(Constants.token, Constants.EncodingAESKey, Constants.appId);

            //  requestMap = MessageUtil.parseEncryptXml(request, msgCrypt, msgSignature, timeStamp, nonce);
            respMessage = processRequestMap(requestMap);

            //  respMessage = msgCrypt.EncryptMsg(respMessage, timeStamp, nonce);
        } catch (Exception e) {
            logger.error("处理微信发送请求（加密流程）异常:", e);
        }
        return respMessage;
    }

    /**
     * 对消息处理
     *
     * @param requestMap
     * @return
     */
    public String processRequestMap(Map<String, String> requestMap) {
        // 返回给微信服务器的消息,默认为null
        String respMessage = "success";
        try {
            logger.info("公众号接收到的消息：" + JSONObject.fromObject(requestMap).toString());
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 默认回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                textMessage.setContent("客户服务电话：\n" +
                        "020 37654947");
                respMessage = MessageUtil.messageToXml(textMessage);

            } else if (msgType.equals(MessageUtil.RESP_MESSAGE_TYPE_IMAGE)) {
                textMessage.setContent("客户服务电话：\n" +
                        "020 37654947");
                respMessage = MessageUtil.messageToXml(textMessage);
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                //事件
                String event = requestMap.get("Event");
                // 事件类型
                String eventKey = requestMap.get("EventKey");

                //关注推送
                if ("subscribe".equals(event)) {
                    logger.info("用户关注公众号");
                    // textMessage.setContent("你好！");
                    textMessage.setContent("HI~欢迎光临彩豆生活圈！\n" +
                            "生活简单一点，优选产品带回家。\n" +
                            "<a href =\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx97fc97c32c6d5c69&redirect_uri=https://info.dsgc.atianqi.com/tz_cdindex.html?ch=1&response_type=code&scope=snsapi_base&state=12369\">开始选购</a>");
                    respMessage = MessageUtil.messageToXml(textMessage);

                } else if ("TEMPLATESENDJOBFINISH".equals(event)) {
                    //模版消息类型
                    logger.info("微信关注用户点击模版消息进入");
                } else if ("unsubscribe".equals(event)) {
                    //模版消息类型
                    logger.info("用户取注公众号");
                }
                //点击事件
                else {

                }
            }

        } catch (Exception e) {
            logger.error("拼装消息异常！", e);
        }
        return respMessage;

    }


    @Value("${weixin.token}")
    private String TOKEN;

    /****
     * 验证消息是否来自微信服务器
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数

     * @return
     */
    private String checkfromwchat(String signature, String timestamp, String nonce) {
        // 1.将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        String content = arr[0].concat(arr[1]).concat(arr[2]);
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;

        return tmpStr;
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String str = "";
        for (int i = 0; i < byteArray.length; i++) {
            str += byteToHexStr(byteArray[i]);
        }
        return str;
    }

    /***
     * 将字节转换为十六进制字符串
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tampArr = new char[2];
        tampArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tampArr[1] = Digit[mByte & 0X0F];
        String str = new String(tampArr);
        return str;
    }

}
