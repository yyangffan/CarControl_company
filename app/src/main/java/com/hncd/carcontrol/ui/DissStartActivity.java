package com.hncd.carcontrol.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.DisStartAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.DisassemablVideo;
import com.hncd.carcontrol.bean.DissVideoListBean;
import com.hncd.carcontrol.bean.FinishDissBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
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

import androidx.annotation.NonNull;
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
    private final int REQUEST_CODE_DISS = 112;
    @BindView(R.id.diss_start_recy)
    RecyclerView mDissStartRecy;
    @BindView(R.id.diss_start_smart)
    SmartRefreshLayout mSmart;
    @BindView(R.id.diss_start_nodata)
    TextView mTvNodata;
    private List<DissVideoListBean.DataBean> mLists;
    private DisStartAdapter mDisStartAdapter;
    private int pageNum = 1;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_diss_start;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        initViews();
    }


    @OnClick({R.id.diss_start_back, R.id.diss_start_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diss_start_back:
                finish();
                break;
            case R.id.diss_start_scan:
                CancelCheckActivity.startMe(this,CancelCheckActivity.REQUEST_CODE_VIDEO);
//                rxPermissionTest();
                break;
        }
    }

    private void initViews() {
        mLists = new ArrayList<>();
        mDisStartAdapter = new DisStartAdapter(this, mLists);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDissStartRecy.setLayoutManager(manager);
        mDissStartRecy.setAdapter(mDisStartAdapter);
        mDisStartAdapter.setOnItemClickListener(new DisStartAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                toFinish(pos);
            }
        });
        mSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmart.setEnableLoadMore(true);
                pageNum = 1;
                mTvNodata.setVisibility(View.GONE);
                getData();
            }
        });
        mSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++pageNum;
                getData();
            }
        });
        getData();
    }

    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", 10);
        map.put("deptId", mLoginBean.getData().getDeptId());
        map.put("userId", mUser_id);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getDisassemablVideoByDeptId(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                DissVideoListBean bean = new Gson().fromJson(result.toString(), DissVideoListBean.class);
                if (bean.getCode() == 200) {
                    if (pageNum == 1) {
                        mLists.clear();
                    }
                    mLists.addAll(bean.getData());
                    mDisStartAdapter.notifyDataSetChanged();
                    if (bean.getData() == null || bean.getData().size() < 10) {
                        mSmart.finishLoadMoreWithNoMoreData();
                    } else {
                        mSmart.setNoMoreData(false);
                    }
                    if(mLists.size() == 0){
                        mTvNodata.setVisibility(View.VISIBLE);
                    }else{
                        mTvNodata.setVisibility(View.GONE);
                    }
                } else {
                    mSmart.finishLoadMoreWithNoMoreData();
                    Toast.makeText(DissStartActivity.this, bean.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
            }
        });


    }

    private void toFinish(int pos) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mLists.get(pos).getID());
        showLoad();
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().endDisassmblVideo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                FinishDissBean bean = new Gson().fromJson(result.toString(), FinishDissBean.class);
                if (bean.getCode() == 200) {
                    mLists.get(pos).setENDTIME(bean.getData().getEndTime());
                    mDisStartAdapter.notifyItemChanged(pos);
                }
                Toast.makeText(DissStartActivity.this, bean.getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pageNum = 1;
        getData();
    }
}