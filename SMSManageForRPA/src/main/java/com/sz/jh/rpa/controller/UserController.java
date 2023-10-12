package com.sz.jh.rpa.controller;

import com.sz.jh.rpa.entity.ResultMsg;
import com.sz.jh.rpa.entity.UserInfo;
import com.sz.jh.rpa.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("jh/user")
@Controller
public class UserController {
    static Logger logger = LoggerFactory.getLogger(UserController.class.getName());
    @Autowired
    private UserInfoService userService;

    @ResponseBody
    @PostMapping("login")
    public ResultMsg loginApp(@org.jetbrains.annotations.NotNull  @RequestBody Map<String, Object> body ) throws IOException, JSONException {
        return userService.LoginApp((String) body.get("userName"), (String) body.get("password"), (String) body.get("android_id"));
    }
}
