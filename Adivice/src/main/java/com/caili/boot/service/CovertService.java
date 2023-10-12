package com.caili.boot.service;

import com.caili.boot.entity.Covert;

import java.util.List;
import java.util.Map;

public interface CovertService {

    List<Map<String,String>> getlist();

    Integer updatenum(Covert covert);
}
