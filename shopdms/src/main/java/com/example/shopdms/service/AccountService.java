package com.example.shopdms.service;

import com.example.shopdms.entity.Account;

import java.util.List;

public interface AccountService {

    //查询所有用户
    List<Account> getlist();

    //根据登录名查用户表(登录)
    Account getpwdbyloginname(String loginname);
}
