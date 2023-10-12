package com.caili.boot.entity;

import java.util.Date;

public class WechatConfig {

    private Integer id;
    private String jsapi_ticket;

    private Date createtime;

    private Date resttime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getResttime() {
        return resttime;
    }

    public void setResttime(Date resttime) {
        this.resttime = resttime;
    }
}
