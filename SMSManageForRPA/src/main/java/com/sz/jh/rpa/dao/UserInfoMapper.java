package com.sz.jh.rpa.dao;

import com.sz.jh.rpa.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface UserInfoMapper {
    //查询用户根据用户名 安卓id 密码
    UserInfo queryUserByStr(String username,  String android_id);
    //查询所有数据
    List<UserInfo> queryAll();

    Boolean updateUserStatus (Integer status,Integer retry,Integer id);

    Boolean reNewUserInfo(String lastLoginTime,Integer id);
}
