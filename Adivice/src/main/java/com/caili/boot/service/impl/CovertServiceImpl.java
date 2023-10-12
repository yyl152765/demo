package com.caili.boot.service.impl;

import com.caili.boot.dao.BaseDao;
import com.caili.boot.dao.CovertMapper;
import com.caili.boot.entity.Area;
import com.caili.boot.entity.Covert;
import com.caili.boot.service.CovertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CovertServiceImpl implements CovertService {

    static Logger logger = LoggerFactory.getLogger(CovertServiceImpl.class.getName());
    static String tablename="o_caidou_covert_k";//表名
    @Autowired
    private BaseDao<CovertMapper> covertMapperBaseDao;

    @Autowired
    private CovertMapper covertMapper;

    @Override
    public List<Map<String, String>> getlist() {
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        map.put("tableName",tablename);
        return covertMapperBaseDao.getAll(map);
    }

    @Override
    public Integer updatenum(Covert covert) {
        return covertMapper.update(covert);
    }
}
