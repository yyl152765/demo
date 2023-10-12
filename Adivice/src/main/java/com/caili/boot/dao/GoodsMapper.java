package com.caili.boot.dao;

import com.caili.boot.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsMapper  extends BaseDao<Goods>{

        Goods getGoodsbyopenid(String id);

}
