package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.caili.boot.entity.Voucher;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.FileExcelService;
import com.caili.boot.service.VoucherService;
import com.caili.boot.util.CryptUtils;
import com.caili.boot.util.FileUtil;
import com.caili.boot.util.Sftp;
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
@RequestMapping("advice")
public class FileController {

    @Autowired
    private FileExcelService fileExcelService;

    @Autowired
    private VoucherService voucherService;







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
        ResultMsg msg = new ResultMsg();
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
        ResultMsg msg = new ResultMsg();
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


    /**
     * excel批量插入兑换券数据
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload/yhq")
    public String add(@RequestParam("file") MultipartFile file){
        Map<Integer, Map<Integer,Object>> map = fileExcelService.addCustomerInfo(file);
        List<Voucher> list=new ArrayList<>();
        System.out.println(map.toString());
        Integer num=1;
        for (Map.Entry<Integer, Map<Integer,Object>> entry : map.entrySet()) {

         /*   Voucher voucher=new Voucher();
            voucher.setVoucherid(String.valueOf(entry.getValue().get(0)));
            voucher.setTypeid(1);
            voucher.setState(0);
            voucher.setCreatetime(CryptUtils.getnowtime());
            list.add(voucher);*/

          Voucher voucher2=new Voucher();
            voucher2.setVoucherid(String.valueOf(entry.getValue().get(1)));
            voucher2.setTypeid(2);
            voucher2.setState(0);
            voucher2.setCreatetime(CryptUtils.getnowtime());
            list.add(voucher2);

          /*    Voucher voucher3=new Voucher();
            voucher3.setVoucherid(String.valueOf(entry.getValue().get(2)));
            voucher3.setTypeid(3);
            voucher3.setState(0);
            voucher3.setCreatetime(CryptUtils.getnowtime());
            list.add(voucher3);*/

            if(list.size()>=10000){
                voucherService.SaveVoucher(list);
                list.clear();
                System.out.println("成功插入10000条数据。。。"+num+"次");
                num++;
            }

        }


        return "success";
    }

 //   @ResponseBody
 //   @RequestMapping("/upload/yhqtxt")
    public String add(){
        File file = new File("C:\\Users\\gzcaili\\Desktop\\彩豆商城1元、3元、5元券\\5元彩金.txt");

        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            List<Voucher> list=new ArrayList<>();
            String s = null;
            Integer num=1;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                Voucher voucher=new Voucher();
                voucher.setVoucherid(s);
                voucher.setTypeid(3);
                voucher.setState(0);
                voucher.setCreatetime(CryptUtils.getnowtime());
                list.add(voucher);
                if(list.size()>=10000){
                    voucherService.SaveVoucher(list);
                    list.clear();
                    System.out.println("成功插入5元兑换券10000条数据。。。"+num+"次");
                    num++;
                }
              //  System.out.println(s);
               // result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return "success";


    }


}
