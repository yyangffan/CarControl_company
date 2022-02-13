package com.hncd.carcontrol.base;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.text.TextUtils;
import android.util.Log;

import com.hncd.carcontrol.utils.CarShareUtil;
import com.ljy.devring.DevRing;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

public class CarApplication extends Application implements CameraXConfig.Provider{
    private static final String TAG = "CarApplication";
    private static volatile CarApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        DevRing.init(this);
        String app_url =(String) CarShareUtil.getInstance().get(CarShareUtil.APP_BASEURL,"");
        if(!TextUtils.isEmpty(app_url)){
            Constant.BASE_URL =app_url;
            initDevring();
        }
//        initDevring();
    }

    public static CarApplication getInstance() {
        return instance;
    }

    public void initDevring() {
        try {
            File cacheDir =new  File(getCacheDir(), "https");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            Log.e(TAG, "init: "+e.toString() );
        }
//        DevRing.init(this);
        DevRing.configureHttp().setBaseUrl(Constant.BASE_URL).setIsUseCookie(true).setConnectTimeout(60).setIsUseLog(Constant.IS_DEBUG);
        DevRing.configureOther().setIsUseCrashDiary(true);
        DevRing.configureImage();
        DevRing.create();
        // 获取uimode系统服务---为深色、夜间模式的相关逻辑
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
//        int currentMode = uiModeManager.getNightMode();
        /*设置夜间状态 UiModeManager.MODE_NIGHT_AUTO ⾃动  UiModeManager.MODE_NIGHT_YES 启⽤*/
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO); // 停⽤
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
