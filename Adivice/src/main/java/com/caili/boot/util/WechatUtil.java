package com.caili.boot.util;


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.*;

public class WechatUtil {

    private static Logger log = LoggerFactory.getLogger(WechatUtil.class);


    private static String CALL_RETURN_URL = "http://www.gzclkeji.com/advice/jsPayResult";


    private static String wxPay = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    public static String wxPay(String appId,String mchId,String fls, String free, String openid, String ip, String type,String title) {
        long start = System.currentTimeMillis();
        SortedMap<String, String> map = new TreeMap<>();
        map.put("appid", appId);
        map.put("attach", title);
        map.put("body", title);
        map.put("mch_id", mchId);
        map.put("nonce_str", getRandomStr(32));
        map.put("notify_url", CALL_RETURN_URL);
        map.put("openid", openid);
        map.put("out_trade_no", fls);
        map.put("spbill_create_ip", ip);

        //map.put("limit_pay", "no_credit");//取消注释则限制信用卡支付
        map.put("total_fee", free);
        map.put("trade_type", type);
        Date day = new Date();
        map.put("time_start", DateUtil.formatDate("yyyyMMddHHmmss", day));
        map.put("time_expire", getExpireTime(day));
        String a = getXml(map);
        String result = HttpRequestUtil.getHttpsResponse(wxPay, "POST", a);
        log.info("微信*支付接口耗时为：" + (System.currentTimeMillis() - start) + "ms");
        return result;
    }


    public static boolean checkParam(Map<String, String> data) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(MD5_STRING);
        String sign = MD5(sb.toString()).toUpperCase();
        return data.get("sign").equals(sign);
    }

    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static final String MD5_STRING = "lottery2004lottery2004lottery200";
    private static String getXml(SortedMap<String, String> map) {
        StringBuilder buf = new StringBuilder((map.size() + 1) * 10);
        SignUtils.buildPayParamsNotNull(buf, map, false);
        String sign = MD5.sign(buf.toString(), "&key=" + MD5_STRING, "utf-8").toUpperCase();
        map.put("sign", sign);
        String a = XmlUtil.parseXML(map);
        return a;
    }



    private static String getExpireTime(Date day) {
        Calendar now = Calendar.getInstance();
        now.setTime(day);
       // now.add(Calendar.MINUTE, 15);
        now.add(Calendar.HOUR,2);
        return DateUtil.formatDate("yyyyMMddHHmmss", now.getTime());
    }




    public static final char[] randomChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    public static String mapJoin(Map<String, String> map, boolean valueUrlEncode) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            if (map.get(key) != null && !"".equals(map.get(key))) {
                String temp = (key.endsWith("_") && key.length() > 1) ? key.substring(0, key.length() - 1) : key;
                sb.append(temp);
                sb.append("=");
                //获取到map的值
                String value = map.get(key);

                sb.append(value);
                sb.append("&");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    public static String getRandomStr(int length) {
        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int a = random.nextInt(36);
            str.append(randomChar[a]);
        }
        return str.toString();
    }




    /**
     * 创建菜单
     *
     * @param menu
     *
     * @param accessToken
     *            有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(String menu, String accessToken,String createUrl) throws JSONException {
        int result = 0;
        String url = createUrl.replace("ACCESS_TOKEN", accessToken);
       // System.out.println(url);
        String str =HttpRequestUtil.post(url,menu);
        if (null != str) {
            JSONObject jsonObject = new JSONObject(new String(str));
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

}
