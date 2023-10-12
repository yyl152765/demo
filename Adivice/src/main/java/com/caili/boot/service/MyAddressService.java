package com.caili.boot.service;

import com.caili.boot.entity.MyAddress;

import java.util.List;
import java.util.Map;

public interface MyAddressService {

  List<Map<String,String>> findlistbyopenid(String openid);

  Integer findcountbyopenid(String openid);

  List<Map<String,String>> findmydefaultaddress(Integer isdefault,String openid);


  Integer saveMyAddress(MyAddress myAddress);

  Integer updateMyaddress(MyAddress myAddress);

}
