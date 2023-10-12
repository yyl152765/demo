package com.example.shopdms.service.impl;

import com.example.shopdms.entity.GoodsType;
import com.example.shopdms.entity.MyAddress;
import com.example.shopdms.repository.BaseRepository;
import com.example.shopdms.service.MyAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyAddressServiceImpl implements MyAddressService {
    static Logger logger = LoggerFactory.getLogger(MyAddressServiceImpl.class.getName());
    @Autowired
    private BaseRepository<MyAddress,Integer> baseRepository;

    static final String TABLENAME="MyAddress";

    @Override
    public MyAddress findbyid(Integer id) {
        logger.info("查询"+MyAddress.class.getName()+"id="+id);
        return baseRepository.findByid(new MyAddress(),id);
    }
}
