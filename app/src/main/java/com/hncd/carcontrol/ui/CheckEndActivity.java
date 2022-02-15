package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.dig_pop.TongdPopWindow;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckEndActivity extends CarBaseActivity {


    @BindView(R.id.check_end_img)
    ImageView mCheckEndImg;
    @BindView(R.id.check_end_tvresult)
    TextView mCheckEndTvresult;
    @BindView(R.id.check_end_name)
    TextView mCheckEndName;
    @BindView(R.id.check_end_haopai)
    TextView mCheckEndHaopai;
    @BindView(R.id.check_end_ywtype)
    TextView mCheckEndYwtype;
    @BindView(R.id.check_end_cytype)
    TextView mCheckEndCytype;
    @BindView(R.id.check_end_td)
    TextView mCheckEndTd;
    @BindView(R.id.check_end_remarks)
    EditText mCheckEndRemarks;
    @BindView(R.id.check_end_date)
    TextView mCheckEndDate;
    @BindView(R.id.check_end_rbno)
    RadioButton mCheckEndRbno;
    @BindView(R.id.check_end_rbyes)
    RadioButton mCheckEndRbyes;
    @BindView(R.id.check_ll)
    LinearLayout mCheckEndLL;

    private String mnowDate;
    private TongdPopWindow mTongdPopWindow;
    private List<CheckAllBean.DataBean.CheckLineBean> mTongd;
    private CheckAllBean mBean;          //上一界面所填数据以及通道数据的填充
    private boolean mState = true;       //查验是否合格
    private CheckAllBean.DataBean mUpBean;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_check_end;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mnowDate = simpleDateFormat.format(new Date());
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String updata = intent.getStringExtra("updata");
        mState = intent.getBooleanExtra("state", true);
        mBean = new Gson().fromJson(data, CheckAllBean.class);
        mUpBean = new Gson().fromJson(updata,CheckAllBean.DataBean.class);

        Log.e(TAG, "init: "+updata );
        initView();
        getData();
    }


    @OnClick({R.id.check_end_back, R.id.check_end_submit, R.id.check_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_end_back:
                finish();
                break;
            case R.id.check_end_submit:
                toComit();
                break;
            case R.id.check_ll:
                initTdPop();
                break;
        }
    }
    private void initView(){
        mCheckEndName.requestFocus();
        mTongd = new ArrayList<>();
        mTongd.addAll(mBean.getData().getCheckLine());
        mCheckEndTd.setText(mTongd.get(0).getLineNo());
    }

    private void getData() {
        mCheckEndImg.setImageResource(mState ? R.drawable.check_success : R.drawable.check_failed);
        mCheckEndTvresult.setText(mState ? "查验合格" : "查验不合格");
        mCheckEndName.setText(mLoginBean.getData().getName());
        mCheckEndCytype.setText("机动车（校车）");
        mCheckEndYwtype.setText("补领登记证书");
        mCheckEndHaopai.setText("大型汽车/你想象不到");
        mCheckEndDate.setText(mnowDate);

    }

    private void toComit() {
        ToastShow("提交结论");
        Map<String, Object> map = new HashMap<>();
        map.put("remarks", mCheckEndRemarks.getText().toString());
        map.put("xny_yes", mCheckEndRbno.isChecked() ? "否" : "是");
        map.put("td", mCheckEndTd.getText().toString());
        Log.e(TAG, "toComit: " + new Gson().toJson(map));


    }

    private void initTdPop() {
        mTongdPopWindow = new TongdPopWindow(this, mTongd, mCheckEndTd.getText().toString(), mCheckEndLL.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        mTongdPopWindow.setOnAdapterClickListener(new TongdPopWindow.OnAdapterClickListener() {
            @Override
            public void onAdapterListener(String name) {
                mCheckEndTd.setText(name);
            }
        });
        mTongdPopWindow.showAsDropDown(mCheckEndLL,0,10);
    }

}
