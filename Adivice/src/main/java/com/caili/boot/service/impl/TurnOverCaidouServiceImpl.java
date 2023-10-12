package com.caili.boot.service.impl;

import com.caili.boot.dao.TurnOverCaidouMapper;
import com.caili.boot.entity.TurnOverCaidou;
import com.caili.boot.service.TurnOverCaidouService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TurnOverCaidouServiceImpl implements TurnOverCaidouService {
    static Logger logger = LoggerFactory.getLogger(TurnOverCaidouServiceImpl.class.getName());

    static String tablename="o_caidou_turnover";//表名
    @Autowired
    private TurnOverCaidouMapper turnOverCaidouMapper;

    @Override
    public int savebill(TurnOverCaidou turnOverCaidou) {
        logger.info("----保存彩豆明细");
        Integer result=turnOverCaidouMapper.save(turnOverCaidou);
        return result;
    }

    @Override
    public List<Map<String, String>> getcaidoumingxi(Integer pagestar, Integer limit, String openid) {
        logger.info("查询彩豆明细");
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        map.put("pages",SqlUtil.page(pagestar,limit,"createtime"));
        return turnOverCaidouMapper.list(map);
    }
}
