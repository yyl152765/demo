package com.caili.boot.entity;

/**
 * 兑换券
 */
public class Voucher {
    private String voucherid;

    private Integer typeid; //类型1 1元彩金  类型2  三元彩金  类型3 五元彩金

    private Integer state; //是否使用

    private String createtime; //创建时间

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "voucherid='" + voucherid + '\'' +
                ", typeid=" + typeid +
                ", state=" + state +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}
