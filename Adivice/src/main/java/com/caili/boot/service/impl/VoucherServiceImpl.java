package com.caili.boot.service.impl;

import com.caili.boot.dao.VoucherMapper;
import com.caili.boot.entity.Voucher;
import com.caili.boot.service.VoucherService;
import com.caili.boot.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoucherServiceImpl implements VoucherService {
    static Logger logger = LoggerFactory.getLogger(VoucherServiceImpl.class.getName());

    @Autowired
    private VoucherMapper voucherMapper;
    static String tablename="o_caidou_voucher";//表名


    @Override
    public Integer SaveVoucher(List<Voucher> voucher) {
        return voucherMapper.saveList(voucher);
    }

    @Override
    public List<Map<String, String>> getVoucher(Integer pagestar, Integer limit, Integer type) {
        logger.info("获取可用的优惠券");
        Map<String,String> prammap=new HashMap<>();  //条件集合 参数名
        Map<String,String> map=new HashMap<>();//带入表名和参数集合
        prammap.put("typeid",String.valueOf(type));
        prammap.put("state",String.valueOf(0));
        String  conditions= SqlUtil.Condition(prammap,tablename);
        map.put("tableName",tablename);
        map.put("condition",conditions);
        map.put("pages",SqlUtil.page(pagestar,limit,"createtime"));
        return voucherMapper.list(map);
    }

    @Override
    public Integer Updatestate(Voucher voucher) {
        return voucherMapper.update(voucher);
    }
}
