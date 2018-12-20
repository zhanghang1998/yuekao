package com.dingtao.week3.model;

import com.dingtao.week3.bean.Goods;
import com.dingtao.week3.bean.Result;
import com.dingtao.week3.bean.Shop;
import com.dingtao.week3.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author dingtao
 * @date 2018/12/6 14:55
 * qq:1940870847
 */
public class CartModel {

    public static Result goodsList() {
        String resultString = HttpUtils.get("http://www.zhaoapi.cn/product/getCarts?uid=71");
        try {
            Gson gson = new Gson();

            Type type = new TypeToken<Result<List<Shop>>>() {
            }.getType();

            Result result = gson.fromJson(resultString, type);
//        Result<List<Goods>> result = new Result<>();
//        result.setCode(0);
//        List<Goods> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            Goods goods = new Goods();
//            goods.setImages("");
//            goods.setTitle("手机"+i);
//            list.add(goods);
//        }
//        result.setData(list);

            return result;
        } catch (Exception e) {

        }
        Result result = new Result();
        result.setCode(-1);
        result.setMsg("数据解析异常");
        return result;
    }

}
