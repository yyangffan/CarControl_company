package com.hncd.carcontrol.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.ApiService;
import com.hncd.carcontrol.base.CarApplication;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigSetActivity extends CarBaseActivity {


    @BindView(R.id.config_set_ip)
    EditText mConfigSetIp;
    @BindView(R.id.config_set_kou)
    EditText mConfigSetKou;
    @BindView(R.id.config_set_name)
    EditText mConfigSetName;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_config_set;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
    }


    @OnClick({R.id.config_set_back, R.id.config_set_save, R.id.config_set_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.config_set_back:
                finish();
                break;
            case R.id.config_set_save:
                toSetUrl();
                break;
            case R.id.config_set_test:
                goTest();
                break;
        }
    }

    /*测试链接-3秒等待时间*/
    private void goTest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.readTimeout(3, TimeUnit.SECONDS).connectTimeout(3, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getUrl())
                .client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<JSONObject> call = service.connectionTest();
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> bean) {
                //请求成功操作
                ToastShow("连接成功");
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                //请求失败操作
                ToastShow("连接失败,请重新配置");
            }
        });
    }

    private void toSetUrl() {
        toSetBaseUrl();
        CarShareUtil.getInstance().put(CarShareUtil.APP_BASEURL, Constant.BASE_URL);
        ToastShow("保存成功");
    }

    /*初始化网络框架*/
    private void toSetBaseUrl() {
        Constant.BASE_URL = getUrl();
        CarApplication.getInstance().initDevring();

    }

    /*拼接出的服务器地址*/
    private String getUrl() {
        String url = "http://";
        url += mConfigSetIp.getText().toString();
        url += ":" + mConfigSetKou.getText().toString();
        url += "/" + mConfigSetName.getText().toString() + "/";
        return url;
    }

}
