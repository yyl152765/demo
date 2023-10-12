package com.caili.boot.entity;

public class TurnOverCaidou {
    private Integer id;

    private String openid;

    private String caidouturnover;

    private String turnover;

    private Integer nowcaidouprice;

    private String createtime;

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TurnOverCaidou{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", caidouturnover='" + caidouturnover + '\'' +
                ", turnover='" + turnover + '\'' +
                ", nowcaidouprice='" + nowcaidouprice + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
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

    public String getCaidouturnover() {
        return caidouturnover;
    }

    public void setCaidouturnover(String caidouturnover) {
        this.caidouturnover = caidouturnover;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public Integer getNowcaidouprice() {
        return nowcaidouprice;
    }

    public void setNowcaidouprice(Integer nowcaidouprice) {
        this.nowcaidouprice = nowcaidouprice;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
