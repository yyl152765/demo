package com.caili.boot.service.impl;

import com.caili.boot.service.FileExcelService;
import com.caili.boot.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
@Service
public class FileExcelServiceImpl  implements FileExcelService {


    @Override
    public Map<Integer, Map<Integer, Object>> addCustomerInfo(MultipartFile file) {
        Map<Integer, Map<Integer,Object>> map = new HashMap<>();
        try {
            map = FileUtil.readExcelContentz(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
