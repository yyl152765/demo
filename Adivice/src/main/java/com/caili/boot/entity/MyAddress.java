package com.caili.boot.entity;

public class MyAddress {
    private Integer  id;
    private String  openid;
    private String  shname;
    private String  phone;
    private String   area;
    private String  areadetail;
    private Integer   isdefault;

    public String getShname() {
        return shname;
    }

    public void setShname(String shname) {
        this.shname = shname;
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

    @Override
    public String toString() {
        return "MyAddress{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", shname='" + shname + '\'' +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", areadetail='" + areadetail + '\'' +
                ", isdefault=" + isdefault +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreadetail() {
        return areadetail;
    }

    public void setAreadetail(String areadetail) {
        this.areadetail = areadetail;
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }
}
