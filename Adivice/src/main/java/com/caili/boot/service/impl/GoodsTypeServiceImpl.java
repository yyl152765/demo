package com.caili.boot.service.impl;

import com.caili.boot.dao.GoodsTypeMapper;
import com.caili.boot.dao.MyAddressMapper;
import com.caili.boot.entity.GoodsType;
import com.caili.boot.service.GoodsTypeService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    static Logger logger = LoggerFactory.getLogger(GoodsTypeServiceImpl.class.getName());

    static String tablename="o_caidou_goods_type";//表名

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public List<Map<String, String>> getGoodsType() {
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        prammap.put("state",String.valueOf(0));
        String  conditions= SqlUtil.Condition(prammap,tablename);

        map.put("tableName",tablename);
        map.put("condition",conditions+"order by createtime desc");
        map.put("pages","");
        return goodsTypeMapper.list(map);
    }

    @Override
    public Integer saveGoodstype(GoodsType goodsType) {
        return goodsTypeMapper.save(goodsType);
    }

    @Override
    public Integer updateGoodstype(GoodsType goodsType) {
        return goodsTypeMapper.update(goodsType);
    }
}
