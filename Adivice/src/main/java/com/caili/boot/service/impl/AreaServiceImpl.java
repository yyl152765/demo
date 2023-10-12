package com.caili.boot.service.impl;

import com.caili.boot.dao.BaseDao;
import com.caili.boot.entity.Area;
import com.caili.boot.service.AreaService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AreaServiceImpl implements AreaService {
    static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class.getName());
    static String tablename="o_caidou_area";//表名
    @Autowired
    private BaseDao<Area> areaBaseDao;

    @Override
    public List<Map<String, String>> getLevelone(Integer pid) {
        logger.info("查询地市信息");
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("pid",String.valueOf(pid));
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        System.out.println(map.toString());
        return areaBaseDao.list(map);
    }

    @Override
    public Map<String, String> getAreaByid(Integer id) {
        logger.info("查询地市信息");
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("pid",String.valueOf(id));
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        return areaBaseDao.findByEntity(map);
    }
}
