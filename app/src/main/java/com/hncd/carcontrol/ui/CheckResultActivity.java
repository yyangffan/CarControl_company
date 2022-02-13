package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.CheckAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.bean.RegistInforBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.utils.ItemRecyDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CheckResultActivity extends CarBaseActivity {

    @BindView(R.id.check_result_recy)
    RecyclerView mCheckResultRecy;
    @BindView(R.id.check_result_smart)
    SmartRefreshLayout mSmart;
    @BindView(R.id.check_result_start)
    Button mBt_start;

    private String data_result = "";
    private List<RegistInforBean.DataBean.RegInfoBean> mMapList;
    private CheckAdapter mCheckAdapter;
    private RegistInforBean mBean;
    private  String check_item = "";

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_check_result;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data_result = extras.getString("data");
        String result_bean = extras.getString("bean");
        mBean = new Gson().fromJson(result_bean, RegistInforBean.class);
        initView();
        getData();
    }

    @OnClick({R.id.check_back, R.id.check_result_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_back:
                finish();
                break;
            case R.id.check_result_start:
                Intent intent = new Intent(this,CheckItemActivity.class);
                intent.putExtra("data",check_item);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void initView() {
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹

        RegistInforBean.DataBean data = mBean.getData();
        Integer auditStatus = data.getAuditStatus();//3可查验  4审核中 5查验完成
        switch (auditStatus) {
            case 3:
                mBt_start.setText("开始查验");
                mBt_start.setBackgroundResource(R.drawable.bg_appcolor);
                break;
            case 4:
                mBt_start.setText("审核中");
                mBt_start.setBackgroundResource(R.drawable.bg_circle_gray);
                mBt_start.setEnabled(false);
                break;
            case 5:
                mBt_start.setText("查验完成");
                mBt_start.setBackgroundResource(R.drawable.bg_circle_gray);
                mBt_start.setEnabled(false);
                break;
        }

        mMapList = new ArrayList<>();
        mMapList.addAll(data.getRegInfo());
        mCheckAdapter = new CheckAdapter(this, mMapList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCheckResultRecy.setLayoutManager(linearLayoutManager);
        mCheckResultRecy.addItemDecoration(new ItemRecyDecoration(this, LinearLayoutManager.VERTICAL));
        mCheckResultRecy.setAdapter(mCheckAdapter);


    }

    /*获取配置项目（查验项目、拍照项目、通道）*/
    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getSetItem(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
                    check_item = result.toString();
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
