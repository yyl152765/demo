package com.example.shopdms.service.impl;

import com.example.shopdms.entity.Account;
import com.example.shopdms.repository.BaseRepository;
import com.example.shopdms.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class.getName());
    @Autowired
    private BaseRepository<Account,Integer> baseRepository;


    static final String TABLENAME="Account";

    @Override
    public List<Account> getlist() {
        return baseRepository.findall(TABLENAME);
    }

    @Override
    public Account getpwdbyloginname(String loginname) {
        logger.info("根据loginname="+loginname+"查询"+TABLENAME);
        return (Account) baseRepository.findObjiectBysql(TABLENAME,"login_admin",loginname);
    }
}
