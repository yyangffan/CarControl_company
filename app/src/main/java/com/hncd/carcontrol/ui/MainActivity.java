package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.MainRecyAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.utils.NotificationsUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CarBaseActivity {


    @BindView(R.id.main_recy)
    RecyclerView mMainRecy;
    @BindView(R.id.main_smart)
    SmartRefreshLayout mSmart;
    private String[][] mMain_strs = new String[][]{{"注销查验", "0"}, {"拆解视频", "1"}, {"个人中心", "2"}};
    private MainRecyAdapter mMainRecyAdapter;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            NotificationsUtils.toConfigMsg(this);
        } else {
            Intent start = new Intent(this, FrontService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(start);
            } else {
                startService(start);
            }
        }
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        initViews();
    }

    @OnClick(R.id.main_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_back:
//                finish();
                break;
        }
    }

    private void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMainRecy.setLayoutManager(gridLayoutManager);
        mMainRecyAdapter = new MainRecyAdapter(this, mMain_strs);
        mMainRecy.setAdapter(mMainRecyAdapter);
        mMainRecyAdapter.setOnItemClickListener(new MainRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                String id = mMain_strs[pos][1];
                whatToGo(id);
            }
        });

    }


    private void whatToGo(String id) {
        switch (id) {
            case "0":
                statActivity(CancelCheckActivity.class);
                break;
            case "1":
                statActivity(DissVideoActivity.class);
                break;
            case "2":
                statActivity(PersonalCenterActivity.class);
                break;


        }


    }

}
