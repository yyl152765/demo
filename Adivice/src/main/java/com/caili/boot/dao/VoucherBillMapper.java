package com.caili.boot.dao;

import com.caili.boot.entity.VoucherBill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface VoucherBillMapper extends BaseDao<VoucherBill> {

}
