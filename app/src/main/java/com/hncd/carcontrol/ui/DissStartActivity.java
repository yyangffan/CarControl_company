package com.hncd.carcontrol.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.DisStartAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.DisassemablVideo;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DissStartActivity extends CarBaseActivity {
    private final int REQUEST_CODE_VIDEO = 111;
    @BindView(R.id.diss_start_recy)
    RecyclerView mDissStartRecy;
    @BindView(R.id.diss_start_smart)
    SmartRefreshLayout mSmart;
    private List<Map<String,Object>> mLists;
    private DisStartAdapter mDisStartAdapter;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_diss_start;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        initViews();
    }


    @OnClick({R.id.diss_start_back, R.id.diss_start_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diss_start_back:
                finish();
                break;
            case R.id.diss_start_scan:
                rxPermissionTest();
                break;
        }
    }

    private void initViews() {
        mLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String,Object> map = new HashMap<>();
            if(i%2==0){
                map.put("edtm","2022-02-24 13:"+(i+10));
            }
            mLists.add(map);
        }
        mDisStartAdapter = new DisStartAdapter(this,mLists);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDissStartRecy.setLayoutManager(manager);
        mDissStartRecy.setAdapter(mDisStartAdapter);
        mDisStartAdapter.setOnItemClickListener(new DisStartAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                toFinish(pos);
            }
        });


        getData();
    }

    private void getData() {


    }

    private void toFinish(int pos){
        ToastShow("已结束");
        Map<String,Object> map = mLists.get(pos);
        map.put("edtm",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
        mDisStartAdapter.notifyItemChanged(pos);


    }


    /*请求相机权限--如果可打开相册需同时请求另一个权限*/
    private void rxPermissionTest() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                    Intent intent = new Intent(DissStartActivity.this, CaptureActivity.class);
                    ZxingConfig config = new ZxingConfig();
                    config.setShowAlbum(false);//是否显示相册
                    config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_VIDEO);

                } else {
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(DissStartActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_CODE_VIDEO:
                    if (data != null) {
                        String content = data.getStringExtra(Constant.CODED_CONTENT);
                        getDisassemablVideo(content);
                    }
                    break;
            }

        }
    }

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
                    Intent intent = new Intent(DissStartActivity.this, DissVideoActivity.class);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }
}