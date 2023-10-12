package com.caili.boot.service.impl;

import com.caili.boot.dao.MyAddressMapper;
import com.caili.boot.dao.TurnOverCaidouMapper;
import com.caili.boot.entity.MyAddress;
import com.caili.boot.service.MyAddressService;
import com.caili.boot.util.SqlUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MyAddressServiceImpl implements MyAddressService {
    static Logger logger = LoggerFactory.getLogger(MyAddressServiceImpl.class.getName());

    static String tablename="o_caidou_myaddress";//表名
    @Autowired
    private MyAddressMapper myAddressMapper;
    @Override
    public List<Map<String, String>> findlistbyopenid(String openid) {
        logger.info("查询我的收货地址");


        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions+ SqlUtil.Orderby("isdefault"));
        map.put("pages","");
        return  myAddressMapper.list(map);
    }

    @Override
    public Integer findcountbyopenid(String openid) {
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        return myAddressMapper.getCountByStr(map);
    }

    @Override
    public  List<Map<String,String>> findmydefaultaddress(Integer isdefault,String openid) {
        logger.info("select * from"+tablename+"where openid="+tablename+"and isdefault="+isdefault);
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("isdefault", String.valueOf(isdefault));
        prammap.put("openid", String.valueOf(openid));
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);

        map.put("condition",conditions);
        map.put("pages","");
        return myAddressMapper.list(map);
    }

    @Override
    public Integer saveMyAddress(MyAddress myAddress) {
        logger.info("----保存收货地址");
        Integer result=myAddressMapper.save(myAddress);
        return result;
    }

    @Override
    public Integer updateMyaddress(MyAddress myAddress) {
        logger.info("----修改默认收货地址");
        Integer result=myAddressMapper.update(myAddress);
        return result;
    }
}
