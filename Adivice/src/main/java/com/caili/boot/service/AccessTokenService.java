package com.caili.boot.service;

import com.caili.boot.dao.BaseDao;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.WechatConfig;
import com.caili.boot.entity.wechat.AccessToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


public interface AccessTokenService{
    //保存用户详情表
    int savewhechatconfig(WechatConfig wechatConfig);

    List<Map<String, String>> getlist();

    Integer findnum();
}
