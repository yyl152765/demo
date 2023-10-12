package com.caili.boot.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileExcelService {
     Map<Integer, Map<Integer,Object>> addCustomerInfo(MultipartFile file);
}
