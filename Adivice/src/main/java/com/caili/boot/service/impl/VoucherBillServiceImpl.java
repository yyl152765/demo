package com.caili.boot.service.impl;

import com.caili.boot.dao.TurnOverCaidouMapper;
import com.caili.boot.dao.VoucherBillMapper;
import com.caili.boot.entity.VoucherBill;
import com.caili.boot.service.VoucherBillService;
import com.caili.boot.util.SqlUtil;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class VoucherBillServiceImpl implements VoucherBillService {
    static Logger logger = LoggerFactory.getLogger(VoucherBillServiceImpl.class.getName());

    static String tablename="o_caidou_voucherbill";//表名
    @Autowired
    private VoucherBillMapper voucherBillMapper;


    @Override
    public Integer saveVoucherBill(VoucherBill voucherBill) {
        return voucherBillMapper.save(voucherBill);
    }

    @Override
    public List<Map<String, String>>  findvoucherbillbByopenid(String openid) {
        logger.info("查询我的兑换券");
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        map.put("pages","");
        return voucherBillMapper.getAll(map);
    }

    @Override
    public Integer findcountbyopenid( String openid) {
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("openid",openid);
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        return voucherBillMapper.getCountByStr(map);
    }
}
