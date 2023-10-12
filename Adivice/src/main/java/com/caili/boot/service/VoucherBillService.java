package com.caili.boot.service;

import com.caili.boot.entity.VoucherBill;

import java.util.List;
import java.util.Map;

public interface VoucherBillService {

    Integer saveVoucherBill(VoucherBill voucherBill);

    List<Map<String, String>> findvoucherbillbByopenid(String openid);

    Integer findcountbyopenid(String map);
}
