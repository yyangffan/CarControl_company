package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.DisassemablVideo;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DissVideoActivity extends CarBaseActivity {


    @BindView(R.id.textView10)
    TextView mTextViewCode;
    @BindView(R.id.diss_video_td)
    TextView mDissVideoTd;
    @BindView(R.id.diss_video_comit)
    Button mDissVideoComit;

    private String data_result;
    private DisassemablVideo bean;

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
                break;
            case R.id.diss_video_comit:
                ToastShow("开始/结束");
                break;
        }
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data_result = extras.getString("data");
        String result_bean = extras.getString("bean");
        bean = new Gson().fromJson(result_bean,DisassemablVideo.class);
        mTextViewCode.setText("流水号：" +data_result);
        mDissVideoTd.setText("通道1");


    }

}