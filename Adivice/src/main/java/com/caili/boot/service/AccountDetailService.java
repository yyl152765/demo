package com.caili.boot.service;


import com.caili.boot.entity.AccountDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface AccountDetailService {
    //保存用户详情表
    int saveAccountDetail(AccountDetail accountDetail);

     Integer findAccountDetailByOpenid(String openid);

     Map<String,String> findallbyopenid(String openid);

     int updateAccountDetail(AccountDetail accountDetail);


}
