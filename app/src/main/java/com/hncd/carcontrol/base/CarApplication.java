package com.hncd.carcontrol.base;

import android.app.Activity;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.dig_pop.NoteDialog;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.ljy.devring.DevRing;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

public class CarApplication extends Application implements CameraXConfig.Provider {
    private static final String TAG = "CarApplication";
    private static volatile CarApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DevRing.init(this);
        String app_url = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_BASEURL, "");
        if (!TextUtils.isEmpty(app_url)) {
            Constant.BASE_URL = app_url;
            initDevring();
        }
        EventBus.getDefault().register(this);
        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());
//        initDevring();
    }

    public static CarApplication getInstance() {
        return instance;
    }

    public void initDevring() {
        try {
            File cacheDir = new File(getCacheDir(), "https");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            Log.e(TAG, "init: " + e.toString());
        }
        DevRing.init(this);
        DevRing.configureHttp().setBaseUrl(Constant.BASE_URL).setIsUseCookie(true).setConnectTimeout(60).setIsUseLog(Constant.IS_DEBUG);
        DevRing.configureOther().setIsUseCrashDiary(true);
        DevRing.configureImage();
        DevRing.create();
        // ??????uimode????????????---???????????????????????????????????????
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
//        int currentMode = uiModeManager.getNightMode();
        /*?????????????????? UiModeManager.MODE_NIGHT_AUTO ??????  UiModeManager.MODE_NIGHT_YES ??????*/
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO); // ??????
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    private NoteDialog mMNoteDialog;  //????????????
    private String note_msg = "";     //????????????
    private Activity mActivity;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsgt(EventMessage msg) {
        if (msg.getMessage().equals("msg")) {
            note_msg= msg.getContent();
//            note_msg = "???????????????" + new Random().nextInt(100);
            showJxqDig();
        }
    }

    private boolean show_note = false;//????????????

    private void showJxqDig() {
        if (mMNoteDialog != null) {//?????????????????????--??????????????????????????????
            countDownTimer.cancel();
            mMNoteDialog.setContent(note_msg);
        } else {
            mMNoteDialog = new NoteDialog(mActivity, note_msg);
        }
        show_note = true;
        if (!mActivity.isFinishing()) {
            mMNoteDialog.show();
        }
        countDownTimer.start();

    }

    class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            mActivity = activity;
            if (show_note && !TextUtils.isEmpty(note_msg)) {//?????????????????????
                if (mMNoteDialog != null) {
                    mMNoteDialog.dismiss();
                }
                mMNoteDialog = new NoteDialog(mActivity, note_msg);
                if ( !mActivity.isFinishing())
                    mMNoteDialog.show();
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            mActivity =activity;

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
        
    }

    private CountDownTimer countDownTimer = new CountDownTimer(5 * 1000, 1 * 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            try {
                Log.i(TAG, "onTick: " + millisUntilFinished / 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            show_note = false;
            if (mMNoteDialog != null && mMNoteDialog.isShowing()) {
                mMNoteDialog.dismiss();
                mMNoteDialog = null;
            }
        }
    };


}
