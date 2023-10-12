package com.example.shopdms.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SqlUtil {
    static Logger logger = LoggerFactory.getLogger(SqlUtil.class.getName());

/*    public static void main(String[] args) {
        Map<String,String> map =new HashMap<>();
        map.put("loginname","admin");
        map.put("password","666");
        String as=Condition(map);
        System.out.println(as);
    }*/

    public static String Orderby(String name) {
        String result = " order by " + name + " desc";

        return result;
    }


    /**
     * 条件查询
     *
     * @return
     */
    public static String Condition(Map<String, String> map, String table) {
        StringBuilder APPEND = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            APPEND.append(entry.getKey());
            APPEND.append(Is());
            APPEND.append(Param(entry.getValue()));
            APPEND.append(And());
            //System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        String str = APPEND.toString();
        String result = str.substring(0, str.length() - 5);
        logger.info("---------------条件查询" + table + "---------------------");
        logger.info("SQL语句:" + result);
        return result;
    }

    /**
     * 分页
     *
     * @param pagestar 起始页
     * @param limit    每页显示的条数
     * @return
     */
    public static String page(Integer pagestar, Integer limit, String time) {
        StringBuilder APPEND = new StringBuilder();
        APPEND.append("order by " + time + " desc limit ");
        APPEND.append(pagestar + "," + limit);
        return APPEND.toString();
    }


    private static String Param(String result) {
        StringBuilder sf = new StringBuilder();
        sf.append("'");
        sf.append(result);
        sf.append("'");
        return sf.toString();
    }


    private static String And() {

        return " and ";
    }

    private static String Is() {

        return "=";
    }

}
