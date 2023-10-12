package com.caili.boot.entity;

public class VoucherBill {
    private Integer id;

    private String openid;

    private String voucherid;

    private Integer typeid; //类型1 1元彩金  类型2  三元彩金  类型3 五元彩金

    private String typename;

    private String createtime;


    @Override
    public String toString() {
        return "VoucherBill{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", voucherid='" + voucherid + '\'' +
                ", typeid=" + typeid +
                ", typename='" + typename + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
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

    public String getVoucherid() {
        return voucherid;
    }

    public void setVoucherid(String voucherid) {
        this.voucherid = voucherid;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
