package com.hncd.carcontrol.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.utils.NotificationsUtils;
import com.superc.yyfflibrary.utils.ToastUtil;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends CarBaseActivity {


    @BindView(R.id.login_sfzcode)
    EditText mLoginSfzcode;
    @BindView(R.id.login_pwd)
    EditText mLoginPwd;
    private String mApp_url;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        mApp_url = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_BASEURL, "");
        if (!TextUtils.isEmpty(mUser_name)) {
            statActivity(MainActivity.class);
            finish();
        }
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            NotificationsUtils.toConfigMsg(this);
        }
    }


    @OnClick({R.id.login_bt, R.id.login_baseset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_bt:
                if (TextUtils.isEmpty(mApp_url)) {
                    ToastShow("请先配置服务器地址");
                    return;
                }
                goLogin();
                break;
            case R.id.login_baseset:
                statActivity(ConfigSetActivity.class);
                break;
        }
    }

    private void goLogin() {
        String sfz = mLoginSfzcode.getText().toString();
        String pwd = mLoginPwd.getText().toString();
        if (TextUtils.isEmpty(sfz)) {
            ToastUtil.showToast(this, "请输入身份证号码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(this, "请输入密码");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userName", sfz);
        map.put("pwd", pwd);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().login(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LoginBean bean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (bean.getCode() == 200) {
                    CarShareUtil.getInstance().put(CarShareUtil.APP_USERINFO,result.toString());
                    CarShareUtil.getInstance().put(CarShareUtil.APP_USERNAME, sfz);
                    CarShareUtil.getInstance().put(CarShareUtil.APP_USERID, bean.getData().getUserIdX());
                    statActivity(MainActivity.class);
                    finish();
                } else {
                    ToastShow(bean.getMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mApp_url = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_BASEURL, "");
    }
}
