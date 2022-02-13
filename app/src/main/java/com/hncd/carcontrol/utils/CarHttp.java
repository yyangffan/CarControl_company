package com.hncd.carcontrol.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hncd.carcontrol.base.ApiService;
import com.hncd.carcontrol.base.CarApplication;
import com.hncd.carcontrol.bean.EventMessage;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.NetworkUtil;
import com.superc.yyfflibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;


public class CarHttp {
    public static final String TAG = "CarHttp";
    private static volatile CarHttp instance;


    private CarHttp() {

    }

    public static CarHttp getInstance() {
        if (instance == null) {
            synchronized (CarHttp.class) {
                if (instance == null) {
                    instance = new CarHttp();
                }
            }
        }
        return instance;
    }

    public ApiService getApiService() {
        return DevRing.httpManager().getService(ApiService.class);
    }

    /*public void toGetDataSig(Observable observable, HttpBackListener backListener) {
        DevRing.httpManager().commonRequest(observable, new CommonObserver<Object>() {
            @Override
            public void onResult(Object result) {
                if (!(result instanceof String)) {
                    try {
                        result = JSONObject.toJSONString(result);
                    } catch (Exception e) {
                        Log.d(TAG, "onResult:加密数据" + e.toString());
                    }
                }
                if (backListener != null) {
                    backListener.onSuccessListener(result);
                }
            }

            @Override
            public void onError(HttpThrowable throwable) {
                if (backListener != null) {
                    String error_msg = throwable.toString();
                    backListener.onErrorLIstener(error_msg);
                    if (error_msg.contains("403")) {
                        EventMessage eventMessage = new EventMessage(403);
                        EventBus.getDefault().post(eventMessage);
                    }
                }
                Log.e(TAG, "onError: " + throwable.toString());
            }
        }, TAG);
    }*/

    public void toGetData(Observable observable, HttpBackListener backListener) {
        if(!NetworkUtil.isNetWorkAvailable(CarApplication.getInstance())){
            ToastUtil.showToast(CarApplication.getInstance(),"网络不可用,请检查网络");
            return;
        }

        DevRing.httpManager().commonRequest(observable, new CommonObserver<JSONObject>() {
            @Override
            public void onResult(JSONObject result) {
                if (backListener != null) {
                    backListener.onSuccessListener(result);
                }
                Log.d(TAG, result.toJSONString());
            }

            @Override
            public void onError(HttpThrowable throwable) {
                 if (backListener != null) {
                    backListener.onErrorLIstener(throwable.toString());
                }
                try {
                    JSONObject jsonObject =JSONObject.parseObject(new Gson().toJson(throwable));
                    JSONObject throwab = jsonObject.getJSONObject("throwable");
                    int code = throwab.getInteger("code");
                    if(code == 401){
                        EventBus.getDefault().post(new EventMessage(1005));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onError: 解析异常= " + e.toString());
                }
                Log.e(TAG, "onError: " + throwable.toString());
//                Log.e(TAG, "onError: " + new Gson().toJson(throwable) );
            }
        }, TAG);
    }


}
