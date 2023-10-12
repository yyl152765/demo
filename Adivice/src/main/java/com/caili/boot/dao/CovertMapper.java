package com.caili.boot.dao;

import com.caili.boot.entity.Covert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CovertMapper extends BaseDao<Covert>{
}
