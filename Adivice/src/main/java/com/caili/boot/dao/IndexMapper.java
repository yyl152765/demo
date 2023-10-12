package com.caili.boot.dao;

import com.caili.boot.entity.Index;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IndexMapper extends BaseDao<Index>{
}
