package com.caili.boot.service.impl;

import com.caili.boot.dao.AccountMapper;
import com.caili.boot.dao.BaseDao;
import com.caili.boot.entity.Account;
import com.caili.boot.service.AccountService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AccountServiceImpl implements AccountService {
    static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class.getName());



    @Autowired
    private AccountMapper accountMapper;



    @Override
    public int Register(Account account) {
        logger.info("---------------注册---------------------");
        int value=accountMapper.save(account);
        return value;
    }

    @Override
    public int update(Account account) {
        return accountMapper.update(account);
    }

    @Override
    public Map<String, String> checkLoginname(String loginname) {
        return null;
    }

   /* @Override
    public Account checkLoginname(String loginname) {
        Map<String,String>  prammap=new HashMap<>();
        prammap.put("loginname",loginname);
        String  conditions=SqlUtil.Condition(prammap,"account");
        map.put("tableName","account");
        map.put("condition",conditions);
        return accountMapper.findByEntity(map);
    }*/

    @Override
    public int findcountbyopenid(String openid) {
        Map<String,String> map=new HashMap<>();
        Map<String,String>  prammap=new HashMap<>();
        prammap.put("openid",openid);
        String  conditions=SqlUtil.Condition(prammap,"o_caidou_account");
        map.put("tableName","o_caidou_account");
        map.put("condition",conditions);
        return accountMapper.getCountByStr(map);
    }

}
