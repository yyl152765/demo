package com.example.shopdms.controller;

import com.alibaba.fastjson.JSON;

import com.example.shopdms.entity.Sftp;
import com.example.shopdms.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Controller
@RequestMapping("shopdms")
public class FileController {











    @Value("${linux.username}")
    private String USERNAME ;

    @Value("${linux.password}")
    private String PASSWORD ;

    @Value("${linux.ip}")
    private String IP ;

    @Value("${linux.port}")
    private String PORT;

    @Value("${linux.id}")
    private Integer ID;


    @Value("${linux.uploadpath}")
    private String imguploadpaths;


    @Value("${linux.filepath}")
    private String filepaths;




    @ResponseBody
    @RequestMapping("/admin/uploadimage")
    public String saveGoodstype(@RequestParam MultipartFile file, HttpServletRequest request) {
        ResponseMessage msg = new ResponseMessage();
        Map<String, String> map =new HashMap<>();
       // Map<String, String> map = FileUtil.uploadimg(file, imguploadpaths);
        Sftp sftpUtil = new Sftp(USERNAME, PASSWORD, IP, ID);
        sftpUtil.toString();
        sftpUtil.login();
        String savePath="";
        try {
            String filename = file.getOriginalFilename();
            System.err.println("图片名称"+filename);
            //保存的文件的名称
            String name= UUID.randomUUID().toString().replace("-", "")+filename.substring(filename.lastIndexOf("."));
            System.out.println(imguploadpaths+"====上传地址===");
            sftpUtil.upload(imguploadpaths, name, file.getInputStream());
            //图片在nginx中的位置
          //  savePath=filepaths+name;
            savePath=name;
            msg.succeed();
            map.put("src", savePath);
            msg.setObj(map);
        } catch (Exception e) {
            System.out.println("上传失败。。。。。");
            e.printStackTrace();
        }finally {
            //释放连接
            sftpUtil.logout();
        }

        return JSON.toJSONString(msg);
    }


    @ResponseBody
    @RequestMapping("/admin/uploadimagearr")
    public String saveGoodstypeLisy(@RequestParam List<MultipartFile> file, HttpServletRequest request) {
        ResponseMessage msg = new ResponseMessage();
        Map<String, String> map =new HashMap<>();
        // Map<String, String> map = FileUtil.uploadimg(file, imguploadpaths);
        Sftp sftpUtil = new Sftp(USERNAME, PASSWORD, IP, ID);
        sftpUtil.toString();
        sftpUtil.login();
        String savePath="";
        try {
            String str="";
            for (int i = 0; i < file.size(); i++) {
                String filename = file.get(i).getOriginalFilename();
                System.err.println("图片名称"+filename);
                //保存的文件的名称
                String name= UUID.randomUUID().toString().replace("-", "")+filename.substring(filename.lastIndexOf("."));
                System.out.println(imguploadpaths+"====上传地址===");
                sftpUtil.upload(imguploadpaths, name, file.get(i).getInputStream());
                //图片在nginx中的位置
               // savePath=filepaths+name;
                savePath=name;
                if(i<(file.size()-1)){
                    str=str+savePath+",";
                }else{
                    str=str+savePath;
                }

            }
            msg.succeed();
            map.put("srcArr", str);
            msg.setObj(map);
            System.err.println(str);
            System.err.println(str.split(",")[0]);
        } catch (Exception e) {
            System.out.println("上传失败。。。。。");
            e.printStackTrace();
        }finally {
            //释放连接
            sftpUtil.logout();
        }

        return JSON.toJSONString(msg);
    }







}
