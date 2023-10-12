package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.config.Systems;
import com.caili.boot.entity.Account;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.Order;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.*;
import com.caili.boot.service.impl.AccountServiceImpl;
import com.caili.boot.util.CheckUtil;
import com.caili.boot.util.CryptUtils;
import com.caili.boot.util.HttpRequestUtil;
import com.caili.boot.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("advice")
@SessionAttributes("account")
public class AccountController {
    static Logger logger = LoggerFactory.getLogger(AccountController.class.getName());
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;


    Integer i = 1;

    @Transactional
    @ResponseBody
    @RequestMapping("demo")
    public String demo(HttpServletRequest request) throws Exception {
        String ss = "{\n" +
                "\t\"openid\": \"oFzVA6I2-QYcbDo3ZyxJMRawzXm8\",\n" +
                "\t\"nickname\": \"lsw\",\n" +
                "\t\"sex\": 1,\n" +
                "\t\"language\": \"zh_CN\",\n" +
                "\t\"city\": \"广州\",\n" +
                "\t\"province\": \"广东\",\n" +
                "\t\"country\": \"中国\",\n" +
                "\t\"headimgurl\": \"https:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/RibS8emC4JzWjzdd1rFUATTlFibwWibpRfyk0BBoXOibFlu6wRoXax9xNqGnPqUtHfmWDHBl5OhxCF33q9EcVleCJg\\/132\",\n" +
                "\t\"privilege\": []\n" +
                "}";
        JSONObject userjson = JSONObject.parseObject(ss);
        System.out.println(userjson.size());
        //String url="https://api.weixin.qq.com/sns/userinfo?access_token=40_8nTftYKx4D67Mqo1fUOFMGBwbZ8cw-Rd3U2wTrpEtYtnBIUPxRKlAZPExbi-3c_tKaN-_jlZ2wsBgpNWaQjQxQ&openid=oFzVA6I2-QYcbDo3ZyxJMRawzXm8&lang=zh_CN";
        //   String userstr = HttpRequestUtil.get(url);
        //   logger.info("得到的用户信息为"+userstr);

        return "121";
    }

    @Value("${weixin.getWebAccessTokenUrl}")
    private String TOKENURL;

    @Value("${weixin.appId}")
    private String APPID;

    @Value("${weixin.appsecret}")
    private String APPSECRET;

    /***
     * 获取用户的openid传递到H5
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/wechat/getopenid")
    public String getopendid(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/caidou/getopenid");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute("openid") != null&&!session.getAttribute("openid").equals("")) {
                logger.info("get session param seccess! key=" + "openid=" + session.getAttribute("openid"));
                msg.succeed();
                Map<String, String> maps = new HashMap<>();
                maps.put("openid", String.valueOf(session.getAttribute("openid")));
                msg.setObj(maps);
                return JSONObject.toJSONString(msg);
            }
            Map<String, String> map = new HashMap<>();
            String code = String.valueOf(body.get("code")); //code
            String state = String.valueOf(body.get("state")); //state
            String openid = "";
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
            // TOKENURL = TOKENURL.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code);
            logger.info("code换取openid 请求地址" + url);
            String str = HttpRequestUtil.get(url);
            logger.info("code换取openid 结果" + str);
            JSONObject json = JSONObject.parseObject(str);

            if (json.size() >= 5) {
                openid = json.getString("openid");
            }
            if (openid != null && !openid.equals("")) {
                Integer num = accountService.findcountbyopenid(openid);
                session.setAttribute("openid", openid);
                msg.succeed();
                map.put("openid", openid);
                msg.setObj(map);
                if (num < 1) {
                    Account account = new Account();
                    account.setOpenid(openid);
                    account.setCreatetime(CryptUtils.getnowtime());
                    account.setState(0);

                    AccountDetail accountDetail = new AccountDetail();
                    accountDetail.setSignday(0);
                    accountDetail.setCaidoubalance(0);
                    accountDetail.setOpenid(openid);
                    accountDetail.setIschange(0);
                    accountDetail.setLastsigntime("1998-04-26 00:00:00");

                    accountService.Register(account);

                    Integer result2 = accountDetailService.saveAccountDetail(accountDetail);

                    // redisUtil.set(id,openid);   //保存用户信息
                    // logger.info("写入redis。。。。");
                    if (result2 == 1) {
                        logger.info("保存用户信息成功");

                    } else {
                        logger.error("保存信息异常。。");
                    }
                }
            } else {
                logger.error("openid为空！！");
            }
        } catch (Exception e) {
            logger.error("获取异常");
        }

        return JSONObject.toJSONString(msg);
    }


    @ResponseBody
    @RequestMapping("/wechat/getuserinfo")
    public String getuserinfo(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/caidou/getuserinfo");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        HttpSession session = request.getSession();
        try {
            Map<String, String> map = new HashMap<>();
            String code = String.valueOf(body.get("code")); //code
            String state = String.valueOf(body.get("state")); //state
            String openid = "";
            String access_token = "";
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
            String str = HttpRequestUtil.post(url, "");
            logger.info("code换取openid 结果" + str);
            JSONObject json = JSONObject.parseObject(str);

            if (json.size() >= 5) {
                openid = json.getString("openid");
                access_token = json.getString("access_token");
            }

            if (openid == null || openid.equals("")) {
                logger.error("获取openid失败！openid为空access_token=" + access_token);
                return JSONObject.toJSONString(msg);
            }

            String userurl = " https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
            String userstr = HttpRequestUtil.get2(userurl);
            logger.info("得到的用户信息为" + userstr);
            JSONObject userjson = JSONObject.parseObject(userstr);
            if (userjson.get("openid").equals(openid)) {
                msg.succeed();
                msg.setObj(userjson);
                return JSONObject.toJSONString(msg);
            }

        } catch (Exception e) {
            logger.error("获取异常");
        }

        return JSONObject.toJSONString(msg);
    }












 /*   @ResponseBody
    @RequestMapping("login")
    public Map<String, Object> login(@RequestBody Account account, HttpServletRequest request, HttpSession session){
        Map<String,Object> resultMap=new HashMap<>();

        if(checkparam(loginname,password)){
            if(CheckUtil.isLetterDigit(loginname)==true&&CheckUtil.isLetterDigit(password)){
             if(checkloginname(loginname)!=null){

                     Map<String,String> checkpwd=checkloginname(loginname);
                     Map<String,String> maos=new HashMap<>();
                     try {
                         String pwd= CryptUtils.GetMD5Code(checkpwd.get("password")) ;
                         if(pwd.equals(password)){
                             resultMap.put("resultCode","000");
                             resultMap.put("resultDesc","登陆成功");
                             resultMap.put("obj",checkpwd);
                             session.setAttribute("advice_user",checkpwd);
                         }else{
                             resultMap.put("resultCode","004");
                             resultMap.put("resultDesc","密码错误");
                             resultMap.put("obj","");
                         }

                     } catch (Exception e) {
                         resultMap.put("resultCode","003");
                         resultMap.put("resultDesc","网络异常");
                         resultMap.put("obj","");
                     }



             }else{
                 resultMap.put("resultCode","002");
                 resultMap.put("resultDesc","用户名不存在");
             }
            }else{
                resultMap.put("resultCode","005");
                resultMap.put("resultDesc","存在非法字符");
            }
        }else{
            resultMap.put("resultCode","001");
            resultMap.put("resultDesc","用户名或密码不能为空,或存在非法字符");
        }

        return resultMap;
    }

 @ResponseBody
    @RequestMapping("register")
    public Map<String, Object> register(@RequestBody Account account, HttpServletRequest request, HttpSession session){
        Map<String,Object> resultMap=new HashMap<>();
        String loginname=account.getLoginname();
        String password=account.getPassword();
        if(checkparam(loginname,password)){
            if(CheckUtil.isLetterDigit(loginname)==true&&CheckUtil.isLetterDigit(password)) {
                Map<String, String> checkpwd = checkloginname(loginname);
                if (checkloginname(loginname) == null) {
                    if(password.length()>6){
                        Integer res=accountService.Register(account);
                        if(res==1){
                            resultMap.put("resultCode", "000");
                            resultMap.put("resultDesc", "注册成功");
                            resultMap.put("obj", account);
                        }else{
                            resultMap.put("resultCode", "005");
                            resultMap.put("resultDesc", "注册失败");
                        }
                    }else{
                        resultMap.put("resultCode", "003");
                        resultMap.put("resultDesc", "密码少于6位");
                    }

                } else {
                    resultMap.put("resultCode", "002");
                    resultMap.put("resultDesc", "用户名已存在");
                }
            }else{
                resultMap.put("resultCode","005");
                resultMap.put("resultDesc","存在非法字符");
            }

        }else{
            resultMap.put("resultCode","001");
            resultMap.put("resultDesc","用户名或密码不能为空");
        }

        return resultMap;
    }

    private boolean checkparam(String loginname,String password){
        boolean flag=false;
        if(loginname!=null &&password!=null){
            if(loginname!="" &&password!=""){

                    flag=true;


            }
        }

        return flag;
    }


        private Map<String,String> checkloginname(String loginname){
            Map<String,String> f= null;
        try {
            Map<String,String> result=accountService.checkLoginname(loginname);
            if(result!=null){
                f=result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }*/
}
