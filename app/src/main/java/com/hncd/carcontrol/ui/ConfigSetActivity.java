package com.hncd.carcontrol.ui;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarApplication;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    private void toSetBaseUrl(){
        String url = "http://";
        url+=mConfigSetIp.getText().toString();
        url+=":"+mConfigSetKou.getText().toString();
        url+="/"+mConfigSetName.getText().toString()+"/";
        Constant.BASE_URL = url;
        CarApplication.getInstance().initDevring();

    }
    /*测试链接*/
    private void goTest(){
        toSetBaseUrl();
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().connectionTest(), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
                    ToastShow("连接成功");
                } else {
                    ToastShow(bean.getMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                ToastShow("连接失败,请重新配置");
            }
        });
    }

    private void toSetUrl(){
        toSetBaseUrl();
        CarShareUtil.getInstance().put(CarShareUtil.APP_BASEURL, Constant.BASE_URL);
        ToastShow("保存成功");

    }

}
