package com.example.shopdms.controller;

import com.example.shopdms.entity.Account;
import com.example.shopdms.entity.Goods;
import com.example.shopdms.entity.GoodsType;
import com.example.shopdms.entity.Index;
import com.example.shopdms.service.GoodsService;
import com.example.shopdms.service.GoodsTypeService;
import com.example.shopdms.util.ResponseMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("shopdms")
public class GoodsController {
    static Logger logger = LoggerFactory.getLogger(GoodsController.class.getName());

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Autowired
    private GoodsService goodsService;


    @ResponseBody
    @PostMapping("savegoods")
    public String savegoods(@RequestBody Goods goods, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        try {
            boolean state = false;
            if (goods.getId() == null) { //添加
                goods.setCreatetime(new Date());
            }
            goods.setCadouparice(goods.getFavorableprice() - 500);
            goods.setCaidounum(5000);
            state = goodsService.save(goods);
            if (state) {
                resp.succeed();
            }
        } catch (Exception e) {
            logger.error("保存失败");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }

    @ResponseBody
    @PostMapping("savegoodstype")
    public String savegoodstype(@RequestBody GoodsType goodsType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        try {
            boolean state = false;
            if (goodsType.getId() == null) { //添加
                goodsType.setCreatetime(new Date());

            }
            state = goodsTypeService.SaveGoodsType(goodsType);
            if (state) {
                resp.succeed();
            }
        } catch (Exception e) {
            logger.error("保存失败");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }


    @ResponseBody
    @PostMapping("getgoods")
    public String getgoods(@RequestBody JSONObject body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        try {
            Integer id = Integer.valueOf(String.valueOf(body.get("id")));
            Goods goods = goodsService.getGoodsType(id);
            com.alibaba.fastjson.JSONObject obj = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(goods);
            //  JSONObject obj = JSONObject.fromObject(goods);
            resp.succeed();
            resp.setObj(obj);
        } catch (Exception e) {
            logger.error("查询出现异常");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }

    @ResponseBody
    @PostMapping("getgoodstype")
    public String getgoodstype(@RequestBody JSONObject body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        logger.info(body.toString());
        try {
            Integer id = Integer.valueOf(String.valueOf(body.get("id")));
            GoodsType goodsType = goodsTypeService.getGoodsTypeByid(id);
            com.alibaba.fastjson.JSONObject obj = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(goodsType);
            //  JSONObject obj = JSONObject.fromObject(goodsType);
            resp.succeed();
            resp.setObj(obj);
        } catch (Exception e) {
            logger.error("查询出现异常");

        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }


    /*****
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("getgoodslist")
    public String getlistbygoodstype(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        try {
            JSONArray data = new JSONArray();
            List<GoodsType> list = goodsTypeService.getGoodsTypelist();
            for (GoodsType goodsType :
                    list) {
                JSONObject one = new JSONObject();
                JSONArray data2 = new JSONArray();
                one.put("title", goodsType.getGoodsname());
                one.put("field", "goodstype");
                one.put("id", goodsType.getId());
                one.put("spread", true);
                List<Goods> list2 = goodsService.getGoodsTypelist(goodsType.getId());

                for (Goods goods :
                        list2) {
                    JSONObject second = new JSONObject();
                    second.put("title", goods.getGoodsname());
                    second.put("field", "goods");
                    second.put("id", goods.getId());
                    data2.add(second);
                }
                one.put("children", data2);
                data.add(one);
            }
            resp.succeed();
            resp.setObj(data);
        } catch (Exception e) {
            logger.error("查询出现异常");

        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }


    @ResponseBody
    @PostMapping("getindeximage")
    public String getindex(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        try {
            Index index=goodsService.getindex();
            String []imgarr=index.getImgarr().split(",");
            String []jumpurlarr=index.getJumpurl().split(",");
            if(imgarr.length!=jumpurlarr.length){
                logger.error("查询轮播图失败，不匹配");
            }
            JSONArray jsonarr=new JSONArray();
            for (int i = 0; i <imgarr.length ; i++) {
                JSONObject js=new JSONObject();
                js.put("imgage",imgarr[i]);
                js.put("url",jumpurlarr[i]);
                jsonarr.add(js);
            }
            resp.succeed();
            resp.setObj(jsonarr);
        } catch (Exception e) {
            logger.error("查询出现异常");

        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }


    @ResponseBody
    @PostMapping("updateindex")
    public String updateindex(@RequestBody Index index, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseMessage resp = new ResponseMessage();
        try {
            String []imgarr=index.getImgarr().split(",");
            String []jumpurlarr=index.getJumpurl().split(",");
            if(imgarr.length!=jumpurlarr.length){
                logger.error("修改轮播图失败，不匹配");
            }
            if(goodsService.updateindex(index.getImgarr(),index.getJumpurl())){
                logger.info("操作成功");
                resp.succeed();
            }
        } catch (Exception e) {
            logger.error("修改出现异常");

        }
        return com.alibaba.fastjson.JSONObject.toJSONString(resp);
    }
}
