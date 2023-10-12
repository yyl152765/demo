package com.example.shopdms.service.impl;


import com.example.shopdms.entity.GoodsType;
import com.example.shopdms.repository.BaseRepository;
import com.example.shopdms.service.GoodsTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    static Logger logger = LoggerFactory.getLogger(GoodsTypeServiceImpl.class.getName());
    @Autowired
    private BaseRepository<GoodsType,Integer> baseRepository;


    static final String TABLENAME="GoodsType";

    @Override
    public List<GoodsType> getGoodsTypelist() {
        logger.info("查询"+GoodsType.class.getName()+"所有正常数据");
        return baseRepository.findBysql(TABLENAME,"state",0);
    }

    @Override
    public Boolean deleteGoodsType(GoodsType goodsType) {
        logger.info("修改"+GoodsType.class.getName()+" ; "+goodsType.getGoodsname()+"类型");
        return baseRepository.update(goodsType);
    }

    @Override
    public Boolean SaveGoodsType(GoodsType goodsType) {
        logger.info("添加"+GoodsType.class.getName()+" ; "+goodsType.toString()+"类型");
        return baseRepository.update(goodsType);
    }

    @Override
    public GoodsType getGoodsTypeByid(Integer id) {
        logger.info("查询"+GoodsType.class.getName()+"id="+id);
        return baseRepository.findByid(new GoodsType(),id);
    }
}
