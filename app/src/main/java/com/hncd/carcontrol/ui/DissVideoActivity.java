package com.hncd.carcontrol.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
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
        mTextViewCode.setText("流水号：" + "12124512412");
        mDissVideoTd.setText("通道1");


    }

}