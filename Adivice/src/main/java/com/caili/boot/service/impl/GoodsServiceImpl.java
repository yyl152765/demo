package com.caili.boot.service.impl;

import com.caili.boot.dao.BaseDao;
import com.caili.boot.dao.GoodsMapper;
import com.caili.boot.entity.Area;
import com.caili.boot.service.GoodsService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class GoodsServiceImpl implements GoodsService {

    static Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class.getName());
    static String tablename="o_caidou_goods";//表名
    @Autowired
    private GoodsMapper goodsMapper;


    @Override
    public List<Map<String, String>> getGoodsBytypeid(Integer typeid) {
        logger.info("select * "+tablename+" where typeid="+typeid);
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("typeid",String.valueOf(typeid));
        prammap.put("state","0");
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions+"order by createtime desc");
        map.put("pages","");
       // System.out.println(map.toString());
        return goodsMapper.list(map);
    }

    @Override
    public Map<String, String> getgoodsbygoodsid(Integer goodsid) {
        logger.info("select * "+tablename+" where id="+goodsid);
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("id",String.valueOf(goodsid));
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        return goodsMapper.findByEntity(map);
    }
}
