package com.caili.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caili.boot.entity.AccountDetail;
import com.caili.boot.entity.Goods;
import com.caili.boot.entity.GoodsType;
import com.caili.boot.entity.Index;
import com.caili.boot.entity.wechat.ResultMsg;
import com.caili.boot.service.GoodsService;
import com.caili.boot.service.GoodsTypeService;
import com.caili.boot.service.IndexService;
import com.caili.boot.util.CheckUtil;
import com.caili.boot.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("advice")
public class GoodsController {
    static Logger logger = LoggerFactory.getLogger(GoodsController.class.getName());

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private IndexService indexService;

    @Value("${linux.filepath}")
    private String filepaths;


    /***
     * 商品详情
     * @param body
     * @param request
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/caidou/getgoods")
    public String getgoods(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/caidou/getgoods");
        logger.info(JSON.toJSONString(body));
        HttpSession session = request.getSession();
        ResultMsg msg = new ResultMsg();
        try {
            Integer goodsid = Integer.valueOf(String.valueOf(body.get("goodsid"))); //openid
            if (session.getAttribute("goods_detail" + goodsid) != null) {
                logger.info("get session param seccess! key=" + "goods_detail" + goodsid);
                msg.succeed();
                msg.setObj(session.getAttribute("goods_detail" + goodsid));
                return JSONObject.toJSONString(msg);
            }
            Map<String, String> map = goodsService.getgoodsbygoodsid(goodsid);
            Goods goods = JSON.parseObject(JSON.toJSONString(map), Goods.class);
            goods.setCadouparice(goods.getCadouparice() / 100);
            goods.setFavorableprice(goods.getFavorableprice() / 100);
            goods.setRetailprice(goods.getRetailprice() / 100);
            goods.setImg(filepaths+  goods.getImg().split(",")[0]);
            String [] arr=  goods.getImgarr().split(",");
            String imgarrs="";
            for (int i = 0; i < arr.length; i++) {
                imgarrs=imgarrs+ filepaths+arr[i];
                if(i!=(arr.length-1)){
                    imgarrs=imgarrs+",";
                }
            }
            goods.setImgarr(imgarrs);
            goods.setTitleimage(filepaths+  goods.getTitleimage().split(",")[0]);
            //  String  goods=   JSONObject.toJSONString(map);

            msg.succeed();
            msg.setObj(net.sf.json.JSONObject.fromObject(goods));
            session.setAttribute("goods_detail" + goodsid, net.sf.json.JSONObject.fromObject(goods));
            session.setMaxInactiveInterval(300);
        } catch (Exception e) {
            logger.error("获取商品信息异常");

        }
        return JSONObject.toJSONString(msg);
    }


    /***
     * 主页展示
     * @param body
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/caidou/index")
    public String index(@RequestBody JSONObject body, HttpServletRequest request) {
        logger.info("进入/caidou/index");
        logger.info(JSON.toJSONString(body));
        ResultMsg msg = new ResultMsg();
        HttpSession session = request.getSession();
        String from = String.valueOf(body.get("from"));
        JSONObject jss = new JSONObject();
/*        try {*/
            if (from.equals("wechat")) {
                if (session.getAttribute("index") != null) {
                    logger.info("get session param seccess! key=index");
                    msg.succeed();
                    msg.setObj(session.getAttribute("index"));
                    return JSONObject.toJSONString(msg);
                }


                List<Map<String, String>> list = indexService.getList();
                for (Map<String, String> map : list) {
                    Index index = JSON.parseObject(JSON.toJSONString(map), Index.class);

                    JSONObject json = new JSONObject();
                    if (!index.getTypename().equals("foot_goods")) {

                        json.put("slideshowImgArr", CheckUtil.getUrlimg(index.getImgarr().split(","), filepaths));
                        json.put("junpmurl", CheckUtil.getUrlimg(index.getJumpurl().split(","), ""));
                        jss.put(index.getTypename(), json);
                    }

                }

                List<Map<String, String>> list1 = goodsTypeService.getGoodsType();
                ArrayList arrs = new ArrayList();
                for (Map<String, String> map : list1) {
                    GoodsType goodsType = JSON.parseObject(JSON.toJSONString(map), GoodsType.class);
                    JSONObject js3 = new JSONObject();
                    js3.put("type_id", goodsType.getId());
                    js3.put("typename", goodsType.getGoodsname());
                    js3.put("typeImg", CheckUtil.getUrlimg(goodsType.getImage().split(","), filepaths));

                    Integer typeid = goodsType.getId();
                    ArrayList arrayList = new ArrayList();
                    List<Map<String, String>> list2 = goodsService.getGoodsBytypeid(typeid);
                    for (Map<String, String> maps : list2) {
                        JSONObject js4 = new JSONObject();
                        Goods goods = JSON.parseObject(JSON.toJSONString(maps), Goods.class);
                        js4.put("goods_id", goods.getId());
                        js4.put("goods_name", goods.getGoodsname());
                        js4.put("retail_price", goods.getRetailprice());
                        js4.put("preferential_price", goods.getFavorableprice());
                        js4.put("caidou_num", goods.getCaidounum());
                        js4.put("caidou_price", goods.getCadouparice());
                        js4.put("goods_imgage", CheckUtil.getUrlimg(goods.getTitleimage().split(","), filepaths));
                        arrayList.add(js4);
                    }
                    js3.put("goods", arrayList);
                    arrs.add(js3);
                }

                jss.put("foot_goods", arrs);
                msg.setObj(jss);
                msg.succeed();
                session.setAttribute("index", jss);
                session.setMaxInactiveInterval(300);
            }

      /*  } catch (Exception e) {
            logger.error("获取参数异常");

        }*/
        return JSONObject.toJSONString(msg);
    }


}
