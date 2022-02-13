package com.hncd.carcontrol.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.RegistInforBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CancelCheckActivity extends CarBaseActivity {


    @BindView(R.id.cancelcheck_back)
    ImageView mCancelcheckBack;
    @BindView(R.id.cancel_check_scan)
    TextView mCancelCheckScan;
    private final int REQUEST_CODE_SCAN = 110;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_cancel_check;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);

    }

    @OnClick({R.id.cancelcheck_back, R.id.cancel_check_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelcheck_back:
                finish();
                break;
            case R.id.cancel_check_scan:
                rxPermissionTest();
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
                    Intent intent = new Intent(CancelCheckActivity.this, CaptureActivity.class);
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
                    Toast.makeText(CancelCheckActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
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
                    Bundle bundle = new Bundle();
                    bundle.putString("data",code);
                    bundle.putString("bean",result.toString());
                    Intent intent = new Intent(CancelCheckActivity.this, CheckResultActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
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


}
