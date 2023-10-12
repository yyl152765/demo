package com.caili.boot.service;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    //查询
    List<Map<String,String>> getGoodsBytypeid(Integer typeid);

    //根据商品id查询
    Map<String,String> getgoodsbygoodsid(Integer goodsid);

}
