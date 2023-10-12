package com.caili.boot.entity.wechat;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WechatBotton {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String media_id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pagepath;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sub_button_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getSub_button_id() {
        return sub_button_id;
    }

    public void setSub_button_id(String sub_button_id) {
        this.sub_button_id = sub_button_id;
    }


}
