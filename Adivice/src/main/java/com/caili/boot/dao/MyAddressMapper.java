package com.caili.boot.dao;

import com.caili.boot.entity.Goods;
import com.caili.boot.entity.MyAddress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MyAddressMapper extends BaseDao<MyAddress> {
    MyAddress getMyaddressbyid(String id);
}
