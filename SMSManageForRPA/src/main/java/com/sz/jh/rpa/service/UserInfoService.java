package com.sz.jh.rpa.service;

import com.sz.jh.rpa.entity.ResultMsg;
import com.sz.jh.rpa.entity.UserInfo;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> test();

    ResultMsg LoginApp(String username, String password, String android_id) throws JSONException;
}
