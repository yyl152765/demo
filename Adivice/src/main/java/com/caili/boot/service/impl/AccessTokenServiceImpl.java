package com.caili.boot.service.impl;

import com.caili.boot.dao.AccountDetailMapper;
import com.caili.boot.dao.WechatConfigMapper;
import com.caili.boot.entity.WechatConfig;
import com.caili.boot.service.AccessTokenService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    static Logger logger = LoggerFactory.getLogger(AccessTokenServiceImpl.class.getName());



    static String tablename="o_caidou_wechatconfig";//表名

    @Autowired
    private WechatConfigMapper wechatConfigMapper;

    @Override
    public int savewhechatconfig(WechatConfig wechatConfig) {
        return wechatConfigMapper.save(wechatConfig);
    }

    @Override
    public List<Map<String, String>> getlist() {
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("1","1");
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions+"order by resttime desc");

        return wechatConfigMapper.list(map);
    }

    @Override
    public Integer findnum() {
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("1","1");
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        return wechatConfigMapper.getCountByStr(map);
    }
}
