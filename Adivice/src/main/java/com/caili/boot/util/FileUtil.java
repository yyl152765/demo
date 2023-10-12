package com.caili.boot.util;


import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.wechat.ResultMsg;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 处理excel表格工具类
 */
public class FileUtil {
    static Logger logger = LoggerFactory.getLogger(FileUtil.class.getName());

    public static Map<Integer, Map<Integer, Object>> readExcelContentz(MultipartFile file) throws Exception {
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();
        // 上传文件名
        Workbook wb = getWb(file);
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {

                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);

        }

        return content;
    }

    //根据Cell类型设置数据
    private static Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                case Cell.CELL_TYPE_FORMULA: {
                 //   if (DateUtil.isCellDateFormatted(cell)) {
                    //    Date date = cell.getDateCellValue();
                  //      cellvalue = date;
                  //  } else {
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                //    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    private static Workbook getWb(MultipartFile mf) {
        String filepath = mf.getOriginalFilename();
        String ext = filepath.substring(filepath.lastIndexOf("."));
        Workbook wb = null;
        try {
            InputStream is = mf.getInputStream();
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return wb;
    }





    /***
     * 单图上传
     * @param file
     * @param imgpath
     * @return
     */
    public static Map<String, String> uploadimg(MultipartFile file, String imgpath) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "001");
        map.put("result", "上传失败");
        if (file.isEmpty()) {
            map.put("code", "001");
            map.put("result", "文件不能为空");
            return map;
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID()+suffixName;
        File file1 = new File(imgpath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File dest = new File(imgpath, fileName);
        try {
            file.transferTo(dest);
            map.put("code", "000");
            map.put("result", dest.getPath());
            System.out.println(dest.getPath());
            return map;
        } catch (IOException e) {
            return map;
        }

    }

    /***
     * 多图上传
     * @param files
     * @param imgpath
     * @return
     */
    public static Map<String, String> uploadimgarr(List<MultipartFile> files, String imgpath) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "001");
        map.put("result", "上传失败");

        if (files == null || files.size() == 0) {
            map.put("result", "文件不能为空");
            return map;
        }
        File file1 = new File(imgpath);
        StringBuilder tmp= new StringBuilder();
        if (!file1.exists()) {
            file1.mkdirs();
        }
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                map.put("result", "上传第" + (i+1) + "文件失败");
                return map;
            }
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID()+suffixName;
            File dest = new File(imgpath, fileName);
            try {
                file.transferTo(dest);
              //  log.info("第" + (i + 1) + "个文件上传成功");
                tmp.append(dest.getPath()).append(",");
            } catch (IOException e) {
                e.printStackTrace();
                map.put("result", "上传第" + (i+1) + "文件失败");
                return map;
            }
        }
        map.put("code","000");
        map.put("result",tmp.toString());
        return map;

    }

}
