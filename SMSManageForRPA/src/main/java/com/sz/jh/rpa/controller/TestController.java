package com.sz.jh.rpa.controller;

import com.sz.jh.rpa.entity.UserInfo;
import com.sz.jh.rpa.service.UserInfoService;
import com.sz.jh.rpa.service.impl.TaskControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequestMapping("user")
@Controller
public class TestController {
    static Logger logger = LoggerFactory.getLogger(TestController.class.getName());
    @Autowired
    private UserInfoService userService;

    @Autowired
    private TaskControlService  taskControlService;

    @ResponseBody
    @RequestMapping("test")
    public List<UserInfo> test(HttpServletRequest request) throws IOException {
       // List<UserInfo> list =userService.test();

        taskControlService.startTask();
        return null;
    }
}
