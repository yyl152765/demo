package com.caili.boot.service.impl;

import com.caili.boot.dao.GoodsTypeMapper;
import com.caili.boot.dao.IndexMapper;
import com.caili.boot.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class IndexServiceImpl implements IndexService {
    static Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class.getName());

    static String tablename="o_caidou_index";//表名

    @Autowired
    private IndexMapper indexMapper;

    @Override
    public List<Map<String, String>> getList() {
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        map.put("tableName",tablename);
        return indexMapper.getAll(map);
    }
}
