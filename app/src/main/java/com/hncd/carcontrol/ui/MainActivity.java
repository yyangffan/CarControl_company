package com.hncd.carcontrol.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
    private final int REQUEST_CODE_SCAN = 110;
    private TextView mTv_red;
    private String down_url = "https://08e121f148084ce20b2c0e2866f93cf6.dlied1.cdntips.net/download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk";

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
    /*    new DownDialog(this,down_url).setOnBtClickListener(new DownDialog.OnBtClickListener() {
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
        }).show();*/
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
            Log.e(TAG, "installApk2: "+e.toString());
        }
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
                rxPermissionTest();
                break;
            case "1":
                statActivity(DissStartActivity.class);
                break;
            case "2":
                statActivity(PersonalCenterActivity.class);
                break;
        }
    }

    /*请求相机权限--如果可打开相册需同时请求另一个权限*/
    private void rxPermissionTest() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    /*ZxingConfig是配置类
                     *可以设置是否显示底部布局，闪光灯，相册，
                     * 是否播放提示音  震动
                     * 设置扫描框颜色等
                     * 也可以不传这个参数
                     * */
                    ZxingConfig config = new ZxingConfig();
                    // config.setPlayBeep(false);//是否播放扫描声音 默认为true
                    //  config.setShake(false);//是否震动  默认为true
                    // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                    config.setShowAlbum(false);//是否显示相册
                    config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);

                } else {
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    if (data != null) {
                        String content = data.getStringExtra(Constant.CODED_CONTENT);
                        toComit(content);
                    }
                    break;
            }

        }
    }

    /*开始查验*/
    private void toComit(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", mUser_name);
        map.put("serialNumber", code);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getRegInfo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                RegistInforBean bean = new Gson().fromJson(result.toString(), RegistInforBean.class);
                if (bean.getCode() == 200) {
                    bean.getData().setDrivingLicenseImg("");//Base64图片不能通过Bundle传递太大会报 android.os.TransactionTooLargeException: data parcel size 624508 bytes
                    Bundle bundle = new Bundle();
                    bundle.putString("data", code);
                    bundle.putString("bean", new Gson().toJson(bean));
                    Intent intent = new Intent(MainActivity.this, CheckResultActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
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

}
