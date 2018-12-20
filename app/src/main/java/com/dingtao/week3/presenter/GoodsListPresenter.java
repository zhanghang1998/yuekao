package com.dingtao.week3.presenter;


import com.dingtao.week3.bean.Result;
import com.dingtao.week3.core.BasePresenter;
import com.dingtao.week3.core.DataCall;
import com.dingtao.week3.model.GoodsListModel;

/**
 * @author dingtao
 * @date 2018/12/6 14:41
 * qq:1940870847
 */
public class GoodsListPresenter extends BasePresenter {

    private int page=1;
    private boolean isRefresh=true;

    public GoodsListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Result getData(Object... args) {
        isRefresh = (boolean) args[0];//是否需要刷新
        if (isRefresh){//刷新
            page = 1;
        }else{
            page++;
        }
        Result result = GoodsListModel.goodsList((String)args[1],page+"");//调用网络请求获取数据
        return result;
    }

    public boolean isResresh(){
        return isRefresh;
    }
}
