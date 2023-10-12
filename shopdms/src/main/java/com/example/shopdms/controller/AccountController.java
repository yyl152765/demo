package com.example.shopdms.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.shopdms.entity.Account;
import com.example.shopdms.entity.GoodsType;
import com.example.shopdms.service.AccountService;
import com.example.shopdms.service.GoodsTypeService;
import com.example.shopdms.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("shopdms")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private GoodsTypeService goodsTypeService;






    @ResponseBody
    @PostMapping("demo")
    public String checkToken(@RequestBody GoodsType goodsType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp=new ResponseMessage();

        System.out.println(goodsTypeService.getGoodsTypeByid(1).toString());
       List<Account> list= accountService.getlist();

        for (Account account:
             list) {
            System.out.println(account.toString());
        }
        return list.toString();
    }



    @ResponseBody
    @RequestMapping("/login")
    public String login(@RequestBody Account account, HttpServletRequest request) throws IOException {
        ResponseMessage resp=new ResponseMessage();
        String loginname=account.getLogin_admin();
        String pwd=account.getPassword_admin();
        if(loginname.contains("ydh")||loginname.contains("admin")){
            resp.setResultDesc("请输入正确的账号和密码");
            return JSONObject.toJSONString(resp);
        }
        Account ac=accountService.getpwdbyloginname(loginname);

        if(ac==null){
            resp.setResultDesc("请输入正确的账号和密码");
            return JSONObject.toJSONString(resp);
        }
       String newpwd= MD5Util.encoding(ac.getPassword_admin());

        if(!newpwd.equals(pwd)){
            resp.setResultDesc("请输入正确的密码");
        }
        resp.succeed();
        resp.setResultDesc("登录成功");
        net.sf.json.JSONObject jsonObj =   net.sf.json.JSONObject.fromObject(ac);
        resp.setObj(jsonObj);

        return JSONObject.toJSONString(resp);
    }
}
