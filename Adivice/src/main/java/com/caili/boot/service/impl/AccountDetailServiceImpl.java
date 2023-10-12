package com.caili.boot.service.impl;

import com.caili.boot.dao.AccountDetailMapper;
import com.caili.boot.dao.AccountMapper;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.service.AccountDetailService;
import com.caili.boot.util.SqlUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountDetailServiceImpl implements AccountDetailService {
    static Logger logger = LoggerFactory.getLogger(AccountDetailServiceImpl.class.getName());



   static String tablename="o_caidou_account_detail";//表名



    @Autowired
    private AccountDetailMapper accountDetailMapper;
    @Override
    public int saveAccountDetail(AccountDetail accountDetail) {
        return accountDetailMapper.save(accountDetail);
    }

    @Override
    public  Integer findAccountDetailByOpenid(String openid) {
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
          Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);


        return  accountDetailMapper.getCountByStr(map);
    }

    @Override
    public Map<String, String> findallbyopenid(String openid) {
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
          Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);


        return accountDetailMapper.findByEntity(map);
    }

    @Override
    public int updateAccountDetail(AccountDetail accountDetail) {
        return accountDetailMapper.update(accountDetail);
    }


}
