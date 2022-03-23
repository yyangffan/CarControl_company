package com.hncd.carcontrol.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.MainRecyAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.DisassemablVideo;
import com.hncd.carcontrol.bean.DownLoadBean;
import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.bean.MessageNoBean;
import com.hncd.carcontrol.bean.RegistInforBean;
import com.hncd.carcontrol.dig_pop.DownDialog;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.utils.NotificationsUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends CarBaseActivity {


    @BindView(R.id.main_recy)
    RecyclerView mMainRecy;
    @BindView(R.id.main_smart)
    SmartRefreshLayout mSmart;
    private String[][] mMain_strs = new String[][]{{"注销查验", "0"}, {"车辆拆解", "1"}, {"个人中心", "2"}};
    private MainRecyAdapter mMainRecyAdapter;
    //    private final int REQUEST_CODE_SCAN = 110;
    private TextView mTv_red;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            NotificationsUtils.toConfigMsg(this);
        } else {
            Intent start = new Intent(this, FrontService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(start);
            } else {
                startService(start);
            }
        }
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        initViews();
        getPdaVersion();
    }

    private void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMainRecy.setLayoutManager(gridLayoutManager);
        mMainRecyAdapter = new MainRecyAdapter(this, mMain_strs);
        mMainRecy.setAdapter(mMainRecyAdapter);
        mMainRecyAdapter.setOnItemClickListener(new MainRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                String id = mMain_strs[pos][1];
                whatToGo(id);
            }
        });
    }


    private void whatToGo(String id) {
        switch (id) {
            case "0":
                CancelCheckActivity.startMe(this, CancelCheckActivity.REQUEST_CODE_SCAN);
                break;
            case "1":
                statActivity(DissStartActivity.class);
                break;
            case "2":
                statActivity(PersonalCenterActivity.class);
                break;
        }
    }

    private boolean is_first = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (is_first) {
            mTv_red = mMainRecy.getLayoutManager().findViewByPosition(2).findViewById(R.id.item_main_num);
            is_first = false;
            getMsgNum();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMsgNum();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsgt(EventMessage msg) {
        if (msg.getMessage().equals("msg")) {
            getMsgNum();
        }
    }


    /*获取未读消息数量*/
    private void getMsgNum() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", mUser_id);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getNoReadMessageNum(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                MessageNoBean bean = new Gson().fromJson(result.toString(), MessageNoBean.class);
                if (bean.getCode() == 200) {
                    Integer num = bean.getData().getNum();
                    if (mTv_red != null) {
                        if (num > 0) {
                            mTv_red.setVisibility(View.VISIBLE);
                            mTv_red.setText(num.toString());
                        } else {
                            mTv_red.setVisibility(View.GONE);
                        }
                    }
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

    private String down_url = "";
    private String versionCode = "";
    /*判断是否需要更新*/
    private void getPdaVersion() {
        try {
            versionCode = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "checkVersion: " + e.toString());
            return;
        }
        Log.e("Okhttp", "getPdaVersion:当前版本 "+versionCode );
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getPdaVersion(), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                DownLoadBean bean = new Gson().fromJson(result.toString(), DownLoadBean.class);
                if (bean.getCode() == 200) {
                    if (bean.getData() != null) {
                        if (!bean.getData().getCurrentVersion().equals(versionCode)) {
                            down_url = bean.getData().getDownLoadPath();
                            if (!TextUtils.isEmpty(down_url))
                                showDownDig();
                        }
                    }

                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    private void showDownDig() {
        new DownDialog(this, down_url).setOnBtClickListener(new DownDialog.OnBtClickListener() {
            @Override
            public void onCancelListener() {
                finish();
            }

            @Override
            public void onUpdateListener() {

            }

            @Override
            public void onDownFinListener(File file) {
                installApk2(file);
            }
        }).show();

    }

    /**
     * 安装apk  适配androidQ
     */
    private void installApk2(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri apkUri = FileProvider.getUriForFile(this, "com.hncd.carcontrol.provider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent);
            finish();
        } catch (Exception e) {
            ToastShow("安装失败");
            Log.e(TAG, "installApk2: " + e.toString());
        }
    }


}
