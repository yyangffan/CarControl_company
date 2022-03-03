package com.hncd.carcontrol.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hncd.carcontrol.bean.BaseBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
     * 获取当前版本号
     *
     * @param body
     * @return
     */
    @POST("cancellation/getPdaVersion")
    Observable<JSONObject> getPdaVersion();

    /**
     * 连接测试
     *
     * @return
     */
    @POST("cancellation/connectionTest")
    Call<JSONObject> connectionTest();

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

    /**
     * 开始拆解
     *
     * @param body
     * @return
     */
    @POST("cancellation/startDisassmblVideo")
    Observable<JSONObject> startDisassmblVideo(@Body RequestBody body);

    /**
     * 结束拆解
     *
     * @param body
     * @return
     */
    @POST("cancellation/endDisassmblVideo")
    Observable<JSONObject> endDisassmblVideo(@Body RequestBody body);

    /**
     * 提交结论
     *
     * @param body
     * @return
     */
    @POST("cancellation/saveInfo")
    Observable<JSONObject> saveInfo(@Body RequestBody body);

    /**
     * 根据流水号获取查验信息（查验开始后先调用）---反显接口调用
     *
     * @param body
     * @return
     */
    @POST("cancellation/getCheckInfo")
    Observable<JSONObject> getCheckInfo(@Body RequestBody body);

    /**
     * 根据单位编号查询拆解视频记录
     *
     * @param body
     * @return
     */
    @POST("cancellation/getDisassemablVideoByDeptId")
    Observable<JSONObject> getDisassemablVideoByDeptId(@Body RequestBody body);

    /**
     * 单文件上传
     *
     * @param map 文件
     * @return
     */
    @Multipart
    @POST("cancellation/uploadImages")
    Observable<JSONObject> upLoadFile(@Part MultipartBody.Part map);


}
