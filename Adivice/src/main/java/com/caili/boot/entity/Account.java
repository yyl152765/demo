package com.caili.boot.entity;

import java.io.Serializable;

public class Account implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String openid;
    private String user_name;
    private String real_name;
    private Integer sex;
    private String phone;
    private String oldopenid;

    private String createtime;

    private Integer state;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOldopenid() {
        return oldopenid;
    }

    public void setOldopenid(String oldopenid) {
        this.oldopenid = oldopenid;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", user_name='" + user_name + '\'' +
                ", real_name='" + real_name + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", oldopenid='" + oldopenid + '\'' +
                '}';
    }
}
