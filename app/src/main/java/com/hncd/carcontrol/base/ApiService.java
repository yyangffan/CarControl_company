package com.hncd.carcontrol.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    /**
     * 登录接口
     *
     * @param body
     * @return
     */
    @POST("cancellation/login")
    Observable<JSONObject> login(@Body RequestBody body);

    /**
     * 连接测试
     *
     * @return
     */
    @POST("cancellation/connectionTest")
    Observable<JSONObject> connectionTest();

    /**
     * 修改密码
     *
     * @param body
     * @return
     */
    @POST("cancellation/upPwd")
    Observable<JSONObject> upPwd(@Body RequestBody body);

    /**
     * 登记信息接口
     *
     * @param body
     * @return
     */
    @POST("cancellation/getRegInfo")
    Observable<JSONObject> getRegInfo(@Body RequestBody body);

    /**
     * 获取配置项目（查验项目、拍照项目、通道）
     *
     * @param body
     * @return
     */
    @POST("cancellation/getSetItem")
    Observable<JSONObject> getSetItem(@Body RequestBody body);

    /**
     * 根据用户ID获取消息未读数量
     *
     * @param body
     * @return
     */
    @POST("cancellation/getNoReadMessageNum")
    Observable<JSONObject> getNoReadMessageNum(@Body RequestBody body);

    /**
     * 根据用户ID更新已读
     *
     * @param body
     * @return
     */
    @POST("cancellation/upMessageReadStatusByUserId")
    Observable<JSONObject> upMessageReadStatusByUserId(@Body RequestBody body);

    /**
     * 获取消息列表
     *
     * @param body
     * @return
     */
    @POST("cancellation/getMessageInfo")
    Observable<JSONObject> getMessageInfo(@Body RequestBody body);

     /**
     * 根据流水号查询拆解视频信息
     *
     * @param body
     * @return
     */
    @POST("cancellation/getDisassemablVideo")
    Observable<JSONObject> getDisassemablVideo(@Body RequestBody body);



}
