package com.example.shopdms.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class SignUtils {
    public static final String MD5_STRING = "lottery2004lottery2004lottery200";
    private static Logger log = LoggerFactory.getLogger(SignUtils.class);
    /** <一句话功能简述>
     * <功能详细描述>验证返回参数
     * @param params
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkParam(Map<String,String> params,String mchKey){
        boolean result = false;
        if(params.containsKey("sign")){
            String sign = params.get("sign");
            params.remove("sign");
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String signRecieve = MD5.sign(preStr, "&key=" + mchKey, "utf-8");
            result = sign.equalsIgnoreCase(signRecieve);
        }
        return result;
    }

    /**
     * 过滤参数
     * @author
     * @param sArray
     * @return
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>(sArray.size());
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /** <一句话功能简述>
     * <功能详细描述>将map转成String
     * @param payParams
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String payParamsToString(Map<String, String> payParams){
        return payParamsToString(payParams,false);
    }

    public static String payParamsToString(Map<String, String> payParams,boolean encoding){
        return payParamsToString(new StringBuilder(),payParams,encoding);
    }
    /**
     * @author
     * @param payParams
     * @return
     */
    public static String payParamsToString(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        buildPayParams(sb,payParams,encoding);
        return sb.toString();
    }

    /**
     * @author
     * @param payParams
     * @return
     */
    public static void buildPayParams(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            if(encoding){
                sb.append(urlEncode(payParams.get(key)));
            }else{
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    /**
     * @author
     * @param payParams
     * @return
     */
    public static void buildPayParamsNotNull(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            if (payParams.get(key).trim().length()>0) {
                sb.append(key).append("=");
                if(encoding){
                    sb.append(urlEncode(payParams.get(key)));
                }else{
                    sb.append(payParams.get(key));
                }
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }


    public static Element readerXml(String body, String encode) throws DocumentException {
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new StringReader(body));
        source.setEncoding(encode);
        Document doc = reader.read(source);
        Element element = doc.getRootElement();
        return element;
    }


    public static boolean checkSign(Map<String, String> map1,String logTittle) {
        try {
            if (!map1.containsKey("time")) {
                log.error(logTittle + "签名验证失败：无时间参数");
                return false;
            }
            long time = Long.valueOf(map1.get("time")).longValue();
            long now = new Date().getTime();
            if (!((now >= time && now - time < 300000) || ( time>= now && time - now < 300000))) {
                log.error(logTittle + "签名验证失败：请求超时");
                return false;
            }
            if (!map1.containsKey("sign")) {
                log.error(logTittle + "签名验证失败：无签名参数");
                return false;
            }
            String md5String1 = sign(map1);
            return map1.get("sign").equals(md5String1);
        } catch (Exception e) {
            return false;
        }
    }

    public static String sign(Map<String, String> map1) {
        SortedMap<String, String> map = new TreeMap<>();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            map.put(entry.getKey(), entry.getValue());
        }
        StringBuilder buf = new StringBuilder((map.size() + 1) * 10);
        SignUtils.buildPayParamsNotNull(buf, map, false);
        String md5String1 = DigestUtils.md5Hex(getContentBytes(buf.toString() + "&key=" + MD5_STRING, "utf-8"))
                .toUpperCase();
        return md5String1;
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
