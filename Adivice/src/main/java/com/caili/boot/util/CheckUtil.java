package com.caili.boot.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CheckUtil {

    /**
     * 验证用户名密码的合法性
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }


    public static List<String> getUrlimg(String[] str,String url){
        List<String> list=new ArrayList<String>();
        for (Integer i=0;i<str.length;i++){
            list.add(url+str[i]);
        }
        return list;
    }


    /***
     * 签到赠送彩豆的规则
     * @param day 签到第几天
     * @return
     */
    public static Integer getcaidounum(Integer day){
        Integer num=0;
        if(day.equals(1)){
            num=10;
        }else  if(day.equals(2)){
            num=20;
        }else  if(day.equals(3)){
            num=40;
        }else  if(day.equals(4)){
            num=80;
        }else  if(day.equals(5)){
            num=150;
        }else  if(day.equals(6)){
            num=200;
        }else  if(day.equals(7)){
            num=500;
        }
        return num;
    }

    /***
     * 彩豆兑换彩金规则
     * @param type 兑换彩金类型
     * @return
     */
    public static Integer getcaijinrule(Integer type){
        Integer num=0;
        if(type.equals(1)){
            num=1000;
        }else  if(type.equals(2)){
            num=2700;
        }else  if(type.equals(3)){
            num=4500;
        }
        return num;
    }




    public static String getcdruledetail(Integer type){
        String str="";
        if(type.equals(1)){
            str="一元彩金";
        }else  if(type.equals(2)){
            str="三元彩金";
        }else  if(type.equals(3)){
            str="五元彩金";
        }
        return str;
    }





    /**
     * 验证对象是否为空
     * @param obj
     * @return
     */
    public static boolean checkObjFieldIsNotNull(Object obj) {   // true 不为空  false 为空
        boolean flag = false;
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) == null || f.get(obj) == "") {
                } else {
                    flag = true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return flag;
    }

    public static String getimg(String str){
        String []imarr= str.split(",");
        String ss="";
        for (int i = 0; i < imarr.length; i++) {
            ss=ss+"http://www.gzclkeji.com/caidouShop/image/"+imarr[i];
            if(i<imarr.length-1){
                ss=ss+",";
            }
        }
        return ss;
    }
    public static boolean isNullAndUndefined(String str){
        boolean flag=false;
        if(str==null||str==""||str.equals("")){
            flag=true;
        }else{
            if(str.equals("null")||str.equals("undefined")){
                flag=true;
            }
        }



        return flag;
    }

}
