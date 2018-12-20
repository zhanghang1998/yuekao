package com.dingtao.week3.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dingtao.week3.bean.Result;

/**
 * @author dingtao
 * @date 2018/12/6 14:57
 * qq:1940870847
 */
public abstract class BasePresenter {

    DataCall dataCall;

    public BasePresenter(DataCall dataCall){
        this.dataCall = dataCall;
    }


    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            Result result = (Result) msg.obj;
            if (result.getCode()==0){
                dataCall.success(result.getData());
            }else{
                dataCall.fail(result);
            }
        }
    };



    public void requestData(final Object...args){
        new Thread(new Runnable() {
            @Override
            public void run() {


                Message message = mHandler.obtainMessage();
                message.obj = getData(args);
                mHandler.sendMessage(message);

            }
        }).start();
    }

    protected abstract Result getData(Object...args);

    public void unBindCall(){
        this.dataCall = null;
    }

}
