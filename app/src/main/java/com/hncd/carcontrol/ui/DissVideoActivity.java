package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.bean.DisassemablVideo;
import com.hncd.carcontrol.dig_pop.TongdPopWindow;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DissVideoActivity extends CarBaseActivity {


    @BindView(R.id.textView10)
    TextView mTextViewCode;
    @BindView(R.id.diss_video_td)
    TextView mDissVideoTd;
    @BindView(R.id.diss_video_comit)
    Button mDissVideoComit;
    @BindView(R.id.diss_video_ll)
    LinearLayout mCheckEndLL;

    private String data_result;
    private DisassemablVideo bean;
    private TongdPopWindow mTongdPopWindow;
    private List<CheckAllBean.DataBean.CheckLineBean> mTongd;
    private String mStatus;
    private String lineId;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_diss_video;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        getData();
    }

    @OnClick({R.id.check_back, R.id.diss_video_ll, R.id.diss_video_comit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.diss_video_ll:
                if (mStatus.equals("0")) {
                    initTdPop();
                }
                break;
            case R.id.diss_video_comit:
                ToastShow("??????/??????");
                switch (mStatus) {
                    case "0":
                        startDisassmblVideo();
                        break;
                    case "1":
                        endDisassmblVideo();
                        break;
                    case "2":
                        ToastShow("?????????");
                        break;
                }
                break;
        }
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data_result = extras.getString("data");
        String result_bean = extras.getString("bean");
        bean = new Gson().fromJson(result_bean, DisassemablVideo.class);
        mTongd = new ArrayList<>();
        mTongd.addAll(bean.getData().getNvrLine());

        //???????????? 0???????????? 1??????????????????  2???????????????
        mStatus = bean.getData().getStatus();
        mTextViewCode.setText("????????????" + data_result);
        switch (mStatus) {
            case "0":
                mDissVideoComit.setText("????????????");
                mDissVideoTd.setText(mTongd.get(0).getLineNo());
                lineId = mTongd.get(0).getId();
                break;
            case "1":
                mDissVideoComit.setText("????????????");
//                mDissVideoTd.setText(mTongd.get(0).getLineNo());//????????????????????????
                mDissVideoTd.setText(bean.getData().getlineNo());
                break;
            case "2":
                mDissVideoComit.setText("???????????????");
//                mDissVideoTd.setText(mTongd.get(0).getLineNo());//????????????????????????
                mDissVideoComit.setBackgroundResource(R.drawable.bg_circle_graya);
                mDissVideoTd.setText(bean.getData().getlineNo());
                break;


        }

    }

    /*????????????*/
    private void startDisassmblVideo() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        map.put("lineId", lineId);
        map.put("deptId", mLoginBean.getData().getDeptId());
        map.put("userId", mUser_id);
        String result = new Gson().toJson(map);
        showLoad();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().startDisassmblVideo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
//                    mStatus = "1"; //????????????????????????
//                    mDissVideoComit.setText("????????????");
//                    getDisassemablVideo(data_result);
                    setResult(RESULT_OK);
                    finish();
                }
                Toast.makeText(DissVideoActivity.this, bean.getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });


    }

    /*????????????*/
    private void endDisassmblVideo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", bean.getData().getId());
        showLoad();
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().endDisassmblVideo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);hideLoad();
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
//                    mStatus = "2"; //????????????????????????
//                    mDissVideoComit.setText("???????????????");
//                    mDissVideoComit.setBackgroundResource(R.drawable.bg_circle_graya);
                    setResult(RESULT_OK);
                    finish();
                }
                Toast.makeText(DissVideoActivity.this, bean.getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);hideLoad();
            }
        });


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
                bean = new Gson().fromJson(result.toString(), DisassemablVideo.class);
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }


    private void initTdPop() {
        mTongdPopWindow = new TongdPopWindow(this, mTongd, mDissVideoTd.getText().toString(), mCheckEndLL.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        mTongdPopWindow.setOnAdapterClickListener(new TongdPopWindow.OnAdapterClickListener() {
            @Override
            public void onAdapterListener(String name, String id) {
                mDissVideoTd.setText(name);
                lineId = id;
            }
        });
        mTongdPopWindow.showAsDropDown(mCheckEndLL, 0, 10);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}