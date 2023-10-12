package com.caili.boot.dao;

import com.caili.boot.entity.Voucher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface VoucherMapper extends BaseDao<Voucher>{
}
