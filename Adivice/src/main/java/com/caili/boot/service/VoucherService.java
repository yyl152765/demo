package com.caili.boot.service;

import com.caili.boot.entity.Voucher;

import java.util.List;
import java.util.Map;

public interface VoucherService {

    Integer SaveVoucher(List<Voucher> voucher);

    List<Map<String,String>> getVoucher(Integer pagestar, Integer limit, Integer type);

    Integer Updatestate(Voucher voucher);
}
