package com.caili.boot.dao;

import com.caili.boot.entity.Voucher;
import com.caili.boot.entity.WechatConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface WechatConfigMapper extends BaseDao<WechatConfig>{
}
