package com.caili.boot.entity;

public class Goods {
    private Integer id;
    private String goodsname;
    private String specifications;
    private String details;
    private String titleimage;
    private String img;
    private String imgarr;
    private Integer retailprice;
    private Integer favorableprice;
    private Integer caidounum;
    private Integer cadouparice;
    private String createtime;
    private Integer state;
    private Integer typeid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitleimage() {
        return titleimage;
    }

    public void setTitleimage(String titleimage) {
        this.titleimage = titleimage;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgarr() {
        return imgarr;
    }

    public void setImgarr(String imgarr) {
        this.imgarr = imgarr;
    }

    public Integer getRetailprice() {
        return retailprice;
    }

    public void setRetailprice(Integer retailprice) {
        this.retailprice = retailprice;
    }

    public Integer getFavorableprice() {
        return favorableprice;
    }

    public void setFavorableprice(Integer favorableprice) {
        this.favorableprice = favorableprice;
    }

    public Integer getCaidounum() {
        return caidounum;
    }

    public void setCaidounum(Integer caidounum) {
        this.caidounum = caidounum;
    }

    public Integer getCadouparice() {
        return cadouparice;
    }

    public void setCadouparice(Integer cadouparice) {
        this.cadouparice = cadouparice;
    }

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

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }


    public String tojsonstr() {
        return "{" +
                "id:" + id +
                ", goodsname:'" + goodsname + '\'' +
                ", specifications:'" + specifications + '\'' +
                ", details:'" + details + '\'' +
                ", goodsimg:'" + titleimage + '\'' +
                ", imgdetail:'" + img + '\'' +
                ", imgarr:'" + imgarr + '\'' +
                ", retailprice:" + retailprice +
                ", favorableprice:" + favorableprice +
                ", caidounum:" + caidounum +
                ", cadouparice:" + cadouparice +
                '}';
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", goodsname='" + goodsname + '\'' +
                ", specifications='" + specifications + '\'' +
                ", details='" + details + '\'' +
                ", titleimage='" + titleimage + '\'' +
                ", img='" + img + '\'' +
                ", imgarr='" + imgarr + '\'' +
                ", retailprice=" + retailprice +
                ", favorableprice=" + favorableprice +
                ", caidounum=" + caidounum +
                ", cadouparice=" + cadouparice +
                ", createtime='" + createtime + '\'' +
                ", state=" + state +
                ", typeid=" + typeid +
                '}';
    }
}
