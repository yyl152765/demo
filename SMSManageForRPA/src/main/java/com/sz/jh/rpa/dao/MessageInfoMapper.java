package com.sz.jh.rpa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageInfoMapper {
    //定时清除多余数据
    boolean DeleteMessageTask(String createtime);

    //写入短信内容
    boolean saveMessage();
}
