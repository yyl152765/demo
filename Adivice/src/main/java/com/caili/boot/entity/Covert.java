package com.caili.boot.entity;

public class Covert {

    private Integer id;

    private Integer type;

    private Integer num;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Covert{" +
                "id=" + id +
                ", type=" + type +
                ", num=" + num +
                '}';
    }
}
