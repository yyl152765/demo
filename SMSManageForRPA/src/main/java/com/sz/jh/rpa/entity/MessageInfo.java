package com.sz.jh.rpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageInfo {
    private Integer id;
    private Integer userid;
    private String smscontent;
    private String smssender;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private Integer status;
    private Integer smstypeid;

}
