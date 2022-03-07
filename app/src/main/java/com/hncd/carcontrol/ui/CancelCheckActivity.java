package com.hncd.carcontrol.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.DisassemablVideo;
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
    @BindView(R.id.cancel_check_edt)
    EditText mCancelcheckEdt;
    @BindView(R.id.textView5)
    TextView mCancelcheckTitle;
    public static final int REQUEST_CODE_SCAN = 110;
    public static final int REQUEST_CODE_VIDEO = 111;
    private int code;

    public static void startMe(Activity activity, int code) {
        Intent intent = new Intent(activity, CancelCheckActivity.class);
        intent.putExtra("code", code);
        activity.startActivity(intent);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_cancel_check;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        code = getIntent().getIntExtra("code", 0);
        if (code == REQUEST_CODE_SCAN) {
            mCancelcheckTitle.setText("注销查验");
        } else if (code == REQUEST_CODE_VIDEO) {
            mCancelcheckTitle.setText("车辆拆解");
        }
    }

    @OnClick({R.id.cancelcheck_back, R.id.diss_start_scan, R.id.cancel_check_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelcheck_back:
                finish();
                break;
            case R.id.diss_start_scan:
                rxPermissionTest();
                break;
            case R.id.cancel_check_search:
                String serck_code = mCancelcheckEdt.getText().toString();
                if (TextUtils.isEmpty(serck_code)) {
                    ToastShow("请输入流水号");
                    return;
                }
                if (code == REQUEST_CODE_SCAN) {
                    toComit(serck_code);
                } else if (code == REQUEST_CODE_VIDEO) {
                    getDisassemablVideo(serck_code);
                }
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
                    ZxingConfig config = new ZxingConfig();
                    config.setShowAlbum(false);//是否显示相册
                    config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, code);

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
                case REQUEST_CODE_VIDEO:
                    if (data != null) {
                        String content = data.getStringExtra(Constant.CODED_CONTENT);
                        getDisassemablVideo(content);
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

    /*拆解接口*/
    private void getDisassemablVideo(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("deptId", mLoginBean.getData().getDeptId());
        map.put("serialNumber", code);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getDisassemablVideo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                DisassemablVideo bean = new Gson().fromJson(result.toString(), DisassemablVideo.class);
                if (bean.getCode() == 200) {
                    Bundle bundle = new Bundle();
                    bundle.putString("data", code);
                    bundle.putString("bean", result.toString());
                    Intent intent = new Intent(CancelCheckActivity.this, DissVideoActivity.class);
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
