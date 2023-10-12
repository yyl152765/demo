package com.example.shopdms.service.impl;

import com.example.shopdms.entity.Goods;
import com.example.shopdms.entity.GoodsType;
import com.example.shopdms.entity.Index;
import com.example.shopdms.repository.BaseRepository;
import com.example.shopdms.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodServiceImpl implements GoodsService {
    static Logger logger = LoggerFactory.getLogger(GoodsTypeServiceImpl.class.getName());
    @Autowired
    private BaseRepository<Goods,Integer> baseRepository;

    @Autowired
    private BaseRepository<Index,Integer> baseRepository2;

    static final String TABLENAME="Goods";
    @Override
    public List<Goods> getGoodsTypelist(Integer typeid) {
        logger.info("查询"+Goods.class.getName()+" typeid="+typeid);
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("typeid=",typeid);
        map.put("state=",0);//正常的状态
        return baseRepository.findByMoreFiled(TABLENAME,map);
    }

    @Override
    public Boolean update(Goods goods) {
        return baseRepository.update(goods);
    }

    @Override
    public Boolean save(Goods goods) {
        return baseRepository.update(goods);
    }

    @Override
    public Goods getGoodsType(Integer id) {
        return baseRepository.findByid(new Goods(),id);
    }

    @Override
    public Index getindex() {
        Integer id=1;
        return baseRepository2.findByid(new Index(),id);
    }

    @Override
    public Boolean updateindex(String imgarr,String jumpurl) {
        Index index=new Index();
        index.setId(1);
        index.setTypename("top_carousel");
        index.setImgarr(imgarr);
        index.setJumpurl(jumpurl);
        return baseRepository2.update(index);
    }
}
