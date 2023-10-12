package com.caili.boot.dao;

import com.caili.boot.entity.AccountDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountDetailMapper extends BaseDao<AccountDetail>{

}
