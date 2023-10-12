package com.caili.boot.service;



import com.caili.boot.entity.Account;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface AccountService {


    //注册
    int Register(Account account);

    int update(Account account);
    //查询用户
    Map<String,String> checkLoginname(String loginname);

    //查询有没有此用户
    int findcountbyopenid(String openid);




}
