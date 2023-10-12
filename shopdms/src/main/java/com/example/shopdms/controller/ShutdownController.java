package com.example.shopdms.controller;

import com.example.shopdms.util.IpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShutdownController {
    @RequestMapping("/shopdmsm/shutdown")
    public String shutdown(HttpServletRequest request) {
        // IpUtil ip工具类 判断是不是本地请求
        System.out.println(request.getRequestURI());
        String ip = IpUtil.getIpBelongAddress(request);
        System.out.println(ip);
        if (ip.indexOf("127.0.0.1") != -1) {
            System.exit(0);
        }
        return "success";
    }
}