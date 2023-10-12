package com.caili.boot.service;

import com.caili.boot.entity.TurnOverCaidou;

import java.util.List;
import java.util.Map;

public interface TurnOverCaidouService {

    int  savebill(TurnOverCaidou turnOverCaidou);

    List<Map<String,String>> getcaidoumingxi(Integer pagestar, Integer limit, String openid);
}
