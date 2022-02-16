package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                finish();
                break;
            case R.id.diss_video_ll:
                if (mStatus.equals("0")) {
                    initTdPop();
                }
                break;
            case R.id.diss_video_comit:
                ToastShow("开始/结束");
                switch (mStatus) {
                    case "0":
                        startDisassmblVideo();
                        break;
                    case "1":
                        endDisassmblVideo();
                        break;
                    case "2":
                        ToastShow("已结束");
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

        //拆解状态 0：未拆解 1：已开始拆解  2：拆解结束
        mStatus = bean.getData().getStatus();
        mTextViewCode.setText("流水号：" + data_result);
        switch (mStatus) {
            case "0":
                mDissVideoComit.setText("开始拆解");
                mDissVideoTd.setText(mTongd.get(0).getLineNo());
                lineId = mTongd.get(0).getId();
                break;
            case "1":
                mDissVideoComit.setText("结束拆解");
//                mDissVideoTd.setText(mTongd.get(0).getLineNo());//需要返回通道名称
                lineId = bean.getData().getLineId();
                break;
            case "2":
                mDissVideoComit.setText("拆解已结束");
//                mDissVideoTd.setText(mTongd.get(0).getLineNo());//需要返回通道名称
                lineId = bean.getData().getLineId();
                mDissVideoComit.setBackgroundResource(R.drawable.bg_circle_graya);
                break;


        }

    }

    /*开始拆解*/
    private void startDisassmblVideo() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        map.put("lineId", lineId);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().startDisassmblVideo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if(bean.getCode() == 200){
                    mStatus = "1"; //修改成已开始拆解
                    mDissVideoComit.setText("结束拆解");
                }
                ToastShow(bean.getMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    /*结束拆解*/
    private void endDisassmblVideo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", bean.getData().getId());
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().endDisassmblVideo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if(bean.getCode() == 200){
                    mStatus = "2"; //修改成拆解已结束
                    mDissVideoComit.setText("拆解已结束");
                    mDissVideoComit.setBackgroundResource(R.drawable.bg_circle_graya);
                }
                ToastShow(bean.getMsg());
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
            public void onAdapterListener(String name,String id) {
                mDissVideoTd.setText(name);
            }
        });
        mTongdPopWindow.showAsDropDown(mCheckEndLL, 0, 10);
    }


}