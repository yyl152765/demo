package com.caili.boot.service;

import java.util.List;
import java.util.Map;

public interface AreaService {

    //获取的地市信息
    List<Map<String,String>> getLevelone(Integer pid);

    //通过主键查询信息
    Map<String,String>  getAreaByid(Integer id);


}
