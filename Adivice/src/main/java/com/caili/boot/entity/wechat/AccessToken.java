package com.caili.boot.entity.wechat;

public class AccessToken {
    private Integer id;
    private String access_token;
    private Integer expires_in;
    private String createtime;

    @Override
    public String toString() {
        return "AccessToken{" +
                "id=" + id +
                ", access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", createtime='" + createtime + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
