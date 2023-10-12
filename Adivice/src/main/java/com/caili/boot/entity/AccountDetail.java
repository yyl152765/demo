package com.caili.boot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountDetail {

    private String openid;

    private Integer ischange;

    private Integer signday;

    private Integer caidoubalance;


    private String lastsigntime;

    public String getLastsigntime() {
        return lastsigntime;
    }

    public void setLastsigntime(String lastsigntime) {
        this.lastsigntime = lastsigntime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getIschange() {
        return ischange;
    }

    public void setIschange(Integer ischange) {
        this.ischange = ischange;
    }

    public Integer getSignday() {
        return signday;
    }

    public void setSignday(Integer signday) {
        this.signday = signday;
    }

    public Integer getCaidoubalance() {
        return caidoubalance;
    }

    public void setCaidoubalance(Integer caidoubalance) {
        this.caidoubalance = caidoubalance;
    }

    @Override
    public String toString() {
        return "AccountDetail{" +
                "openid='" + openid + '\'' +
                ", ischange=" + ischange +
                ", signday=" + signday +
                ", caidoubalance=" + caidoubalance +
                ", lastsigntime='" + lastsigntime + '\'' +
                '}';
    }
}
