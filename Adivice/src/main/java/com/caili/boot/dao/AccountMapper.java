package com.caili.boot.dao;

import com.caili.boot.entity.Account;
import com.caili.boot.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface AccountMapper extends BaseDao<Account>{

}
