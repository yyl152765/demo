package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.MyAddress;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.AreaService;
import com.caili.boot.service.MyAddressService;
import com.caili.boot.util.CheckUtil;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Controller
@RequestMapping("advice")
@SessionAttributes("myaddress")
public class AreaController {
    static Logger logger = LoggerFactory.getLogger(AreaController.class.getName());

    @Autowired
    private AreaService areaService;

    @Autowired
    private MyAddressService myAddressService;

    /***
     * 通过地市拿到相关信息
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/account/getarea")
    public String getarea(HttpServletRequest request) throws Exception {
        ResultMsg msg = new ResultMsg();

        try {
            Integer pid = Integer.valueOf(request.getParameter("pid"));
            List<Map<String, String>> list = areaService.getLevelone(pid);
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

    /**
     * 获取收获地址
     *
     * @param request openid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/account/getmyaddress")
    public String getmyaddress(HttpServletRequest request) throws Exception {
        ResultMsg msg = new ResultMsg();

        try {
            String openid = request.getParameter("openid");
            if (CheckUtil.isNullAndUndefined(openid)) {
                logger.error("openid异常,openid=" + openid);
                return JSONObject.toJSONString(msg);
            }
            Integer count = myAddressService.findcountbyopenid(openid);
            JSONArray json = new JSONArray();
            if (count < 1) {
                msg.succeed();
                msg.setObj(json);
                return JSONObject.toJSONString(msg);
            }
            List<Map<String, String>> list = myAddressService.findlistbyopenid(openid);
            for (Map<String, String> maps :
                    list) {
                json.add(JSONObject.toJSONString(maps));
            }
            msg.succeed();
            msg.setObj(JSONObject.toJSON(json));
        } catch (Exception e) {
            logger.error("查询收货地址异常");

        }
        return JSONObject.toJSONString(msg);
    }

    /***
     * 添加收货地址或更新收货地址
     * @param myAddress
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/account/savemyaddress")
    public String saveMyaddress(@RequestBody MyAddress myAddress, Model model) throws Exception {
        logger.info("进入/caidou/signin");
        logger.info(myAddress.toString());
        ResultMsg msg = new ResultMsg();
        try {
            if (!checkmyaddress(myAddress)) {//验证参数是否正确
                logger.error("参数输入异常");
                return JSONObject.toJSONString(msg);
            }
            Integer count = myAddressService.findcountbyopenid(myAddress.getOpenid());
            if (count > 0) {
                List<Map<String, String>> list = myAddressService.findmydefaultaddress(1,myAddress.getOpenid());
                Integer flags;
                if (list.size() == 1) {
                    MyAddress my = JSON.parseObject(JSON.toJSONString(list.get(0)), MyAddress.class);
                    my.setIsdefault(0);

                    flags = myAddressService.updateMyaddress(my);
                    if (flags == 1) {
                        logger.info("之前默认地址更新");
                    }
                }

                Integer flag;
                if (myAddress.getId() != null) {
                    logger.info("修改中。。");
                    flag = myAddressService.updateMyaddress(myAddress);
                } else {
                    logger.info("保存中");
                    flag = myAddressService.saveMyAddress(myAddress);
                }

                if (flag == 1) {
                    logger.info("更新成功");
                    msg.succeed();
                }


            }else{
                logger.info("保存中");
              Integer  flags = myAddressService.saveMyAddress(myAddress);
                if (flags == 1) {
                    logger.info("保存成功");
                    msg.succeed();
                }
            }

        } catch (Exception e) {
            logger.error("修改收货地址失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();


        }


        return JSONObject.toJSONString(msg);
    }


    /**
     * 验证参数输入是否正确
     *
     * @param myAddress
     * @return
     */
    private boolean checkmyaddress(MyAddress myAddress) {
        boolean flag = true;

        String phone = myAddress.getPhone();
        String openid = myAddress.getOpenid();
        String shname = myAddress.getArea();
        String area = myAddress.getArea();
        // String areadetail=myAddress.getAreadetail();
        // Integer areacode=myAddress.getIsdefault();
        Pattern p = Pattern.compile("^1\\d{10}$");
        if (phone == null || !p.matcher(phone).matches()) {
            flag = false;
            return flag;
        }
        if (openid == null) {
            flag = false;
            return flag;
        }
        if (shname == null) {
            flag = false;
            return flag;
        }
        if (area == null || area.split("-").length < 3) {
            flag = false;
            return flag;
        }


        return flag;
    }


}
