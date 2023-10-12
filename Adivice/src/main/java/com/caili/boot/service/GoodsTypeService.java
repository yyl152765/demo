package com.caili.boot.service;

import com.caili.boot.entity.GoodsType;

import java.util.List;
import java.util.Map;

public interface GoodsTypeService {

    List<Map<String,String>>  getGoodsType();

    Integer saveGoodstype(GoodsType goodsType);

    Integer  updateGoodstype(GoodsType goodsType);
}
