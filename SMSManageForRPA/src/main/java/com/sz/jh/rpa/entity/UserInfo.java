package com.sz.jh.rpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Integer id;
    private String  username;
    private String password;
    private String androidid;
    private String phonebrand;
    private String  phonemodel;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private Integer status;
    private Integer retrynum;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastlogintime;
}
