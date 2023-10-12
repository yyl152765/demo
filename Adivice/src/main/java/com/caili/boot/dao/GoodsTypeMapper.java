package com.caili.boot.dao;

import com.caili.boot.entity.GoodsType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GoodsTypeMapper extends BaseDao<GoodsType>{

}
