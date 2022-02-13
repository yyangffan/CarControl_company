package com.hncd.carcontrol.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.base.BaseActivity;

import androidx.annotation.Nullable;

public abstract class CarBaseActivity extends BaseActivity {

    public String mUser_name ="",mUser_id = "";
    public LoginBean mLoginBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String user_Info = (String)CarShareUtil.getInstance().get(CarShareUtil.APP_USERINFO, "");
        if(!TextUtils.isEmpty(user_Info)){
            mLoginBean = new Gson().fromJson(user_Info,LoginBean.class);
        }
        mUser_name = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERNAME, "");
        mUser_id = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERID, "");
        super.onCreate(savedInstanceState);
        if(DevRing.activityListManager()!=null)
        DevRing.activityListManager().addActivity(this);

    }
}
