package com.caili.boot.entity;

import java.util.List;

public class Order {
    private Integer id;
    private String openid;
    private String ordernumber;
    private Double payprice;
    private Integer ispay;
    private Integer isship;
    private String shiptime;
    private Integer isreceipts;
    private String shipnumber;
    private Integer state;
    private String createtime;
    private String updatetime;
    private Integer iscovert;
    private Integer covert_caidou;
    private Integer covert_money;

    private String prepay_id;

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    private List<OrderGoods> orderGoods;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", ordernumber='" + ordernumber + '\'' +
                ", payprice=" + payprice +
                ", ispay=" + ispay +
                ", isship=" + isship +
                ", shiptime='" + shiptime + '\'' +
                ", isreceipts=" + isreceipts +
                ", shipnumber='" + shipnumber + '\'' +
                ", state=" + state +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", iscovert=" + iscovert +
                ", covert_caidou=" + covert_caidou +
                ", covert_money=" + covert_money +
                ", orderGoods=" + orderGoods +
                '}';
    }

    public List<OrderGoods> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<OrderGoods> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Double getPayprice() {
        return payprice;
    }

    public void setPayprice(Double payprice) {
        this.payprice = payprice;
    }

    public Integer getIspay() {
        return ispay;
    }

    public void setIspay(Integer ispay) {
        this.ispay = ispay;
    }

    public Integer getIsship() {
        return isship;
    }

    public void setIsship(Integer isship) {
        this.isship = isship;
    }

    public String getShiptime() {
        return shiptime;
    }

    public void setShiptime(String shiptime) {
        this.shiptime = shiptime;
    }

    public Integer getIsreceipts() {
        return isreceipts;
    }

    public void setIsreceipts(Integer isreceipts) {
        this.isreceipts = isreceipts;
    }

    public String getShipnumber() {
        return shipnumber;
    }

    public void setShipnumber(String shipnumber) {
        this.shipnumber = shipnumber;
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

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getIscovert() {
        return iscovert;
    }

    public void setIscovert(Integer iscovert) {
        this.iscovert = iscovert;
    }

    public Integer getCovert_caidou() {
        return covert_caidou;
    }

    public void setCovert_caidou(Integer covert_caidou) {
        this.covert_caidou = covert_caidou;
    }

    public Integer getCovert_money() {
        return covert_money;
    }

    public void setCovert_money(Integer covert_money) {
        this.covert_money = covert_money;
    }
}
