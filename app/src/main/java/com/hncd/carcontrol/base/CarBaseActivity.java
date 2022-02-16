package com.hncd.carcontrol.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.dig_pop.NoteDialog;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

public abstract class CarBaseActivity extends BaseActivity {

    public String mUser_name = "", mUser_id = "";
    public LoginBean mLoginBean;
    private NoteDialog mMNoteDialog;  //提示弹窗

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String user_Info = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERINFO, "");
        if (!TextUtils.isEmpty(user_Info)) {
            mLoginBean = new Gson().fromJson(user_Info, LoginBean.class);
        }
        mUser_name = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERNAME, "");
        mUser_id = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERID, "");
        super.onCreate(savedInstanceState);
        if (DevRing.activityListManager() != null)
            DevRing.activityListManager().addActivity(this);
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsgt(EventMessage msg) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}
