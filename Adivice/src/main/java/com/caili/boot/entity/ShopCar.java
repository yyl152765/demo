package com.caili.boot.entity;

public class ShopCar {
    private Integer id;
    private String openid;
    private Integer goodsid;
    private Integer num;
    private Integer state;
    private String createtime;
    private String updatetime;
    private Goods goods;


    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
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

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "ShopCar{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", goodsid=" + goodsid +
                ", num=" + num +
                ", state=" + state +
                ", createtime='" + createtime + '\'' +
                ", goods=" + goods +
                '}';
    }
}
