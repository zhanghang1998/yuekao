package com.dingtao.week3.presenter;


import com.dingtao.week3.bean.Result;
import com.dingtao.week3.core.BasePresenter;
import com.dingtao.week3.core.DataCall;
import com.dingtao.week3.model.CartModel;
import com.dingtao.week3.model.GoodsListModel;

/**
 * @author dingtao
 * @date 2018/12/6 14:41
 * qq:1940870847
 */
public class CartPresenter extends BasePresenter {


    public CartPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Result getData(Object... args) {
        Result result = CartModel.goodsList();//调用网络请求获取数据
        return result;
    }

}
