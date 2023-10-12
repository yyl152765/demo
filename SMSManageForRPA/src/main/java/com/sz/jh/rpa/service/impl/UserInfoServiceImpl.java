package com.sz.jh.rpa.service.impl;

import com.sz.jh.rpa.util.MD5Util;
import com.sz.jh.rpa.dao.UserInfoMapper;
import com.sz.jh.rpa.entity.ResultMsg;
import com.sz.jh.rpa.entity.UserInfo;
import com.sz.jh.rpa.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userMapper;
    static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class.getName());
    @Override
    public List<UserInfo> test() {
        String username = "admin111";
        String android_id ="0270811137f4f092";
        List<UserInfo> list =new ArrayList<>();
        UserInfo userList =userMapper.queryUserByStr(username,android_id);
        list.add(userList);
        return list;
    }

    @Override
    public ResultMsg LoginApp(String username, String password, String android_id) throws JSONException {
        //通过用户名和安卓id查询数据
        UserInfo userInfo = userMapper.queryUserByStr(username,android_id);
        ResultMsg msg =new ResultMsg();
        Map<String,Object> obj = new HashMap<>();
        boolean resp =false;
        if (userInfo !=null){
            //查询密码加密参数
           String newPassword = MD5Util.getMD5Str(userInfo.getPassword());
           if (newPassword.equals(password)){
               //密码匹配成功
               Integer status = userInfo.getStatus();
               if (status.equals(1)){
                   SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   String lastLoginTime = sdf.format(new Date());
                   try {
                       resp =  userMapper.reNewUserInfo(lastLoginTime,userInfo.getId());
                   }catch (Exception ignored){
                       logger.error("更新登录信息出现异常！！");
                   }
                   if (resp)
                       //登录成功
                       msg.succeed();
               }else {
                   msg.setResultCode("004");
                   msg.setResultDesc("此账号已被锁！");
                   obj.put("status",userInfo.getStatus());
                   obj.put("retry",userInfo.getRetrynum());
                   msg.setObj(obj);
               }
           }else {
               //密码输入不正确
               Integer retryNum = userInfo.getRetrynum();
               Integer status = userInfo.getStatus();
               if (retryNum > 0)
                   retryNum = retryNum - 1; //重试次数-1
               else
                   status = 2; //锁定账号

               try {
                   resp =  userMapper.updateUserStatus(status,retryNum,userInfo.getId());
               }catch (Exception ignored){
                   logger.error("修改账号数据出现异常！！");
               }
               if (resp){
                   msg.setResultCode("003");
                   msg.setResultDesc("密码输入错误！");
                   obj.put("retry",retryNum);
                   obj.put("status",userInfo.getStatus());
                   msg.setObj(obj);
               }

           }
        }else {
            msg.setResultCode("002");
            msg.setResultDesc("用户名或密码错误！");
        }
        return msg;
    }
}
