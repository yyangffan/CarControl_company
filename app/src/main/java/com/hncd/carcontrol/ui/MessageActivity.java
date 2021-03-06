package com.hncd.carcontrol.ui;

import android.view.View;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.MessageAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.bean.MessageListBean;
import com.hncd.carcontrol.dig_pop.NoteDialog;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.utils.ItemRecyDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MessageActivity extends CarBaseActivity {

    @BindView(R.id.message_recy)
    RecyclerView mMessageRecy;
    @BindView(R.id.message_smart)
    SmartRefreshLayout mSmart;
    private List<MessageListBean.DataBean> mListMsgs;
    private MessageAdapter mMessageAdapter;
    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        initViews();
    }

    @OnClick(R.id.message_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message_back:
                finish();
                break;
        }
    }

    private void initViews() {
        mListMsgs = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, mListMsgs);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mMessageRecy.setLayoutManager(manager);
        mMessageRecy.addItemDecoration(new ItemRecyDecoration(this, LinearLayoutManager.VERTICAL));
        mMessageRecy.setAdapter(mMessageAdapter);
        getData();
        upReadMsg();
        mSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmart.setEnableLoadMore(true);
                pageNum = 1;
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

     /*   mSmart.setEnablePureScrollMode(true);//???????????????????????????
        mSmart.setEnableNestedScroll(true);//????????????????????????;
        mSmart.setEnableOverScrollDrag(true);//?????????????????????????????????????????????1.0.4
        mSmart.setEnableOverScrollBounce(true);//????????????????????????*/
    }

    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", mUser_id);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getMessageInfo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                mSmart.finishRefresh();
                mSmart.finishLoadMore();
                MessageListBean bean = new Gson().fromJson(result.toString(), MessageListBean.class);
                if (bean.getCode() == 200) {
                    if (pageNum == 1) {
                        mListMsgs.clear();
                    }
                    mListMsgs.addAll(bean.getData());
                    mMessageAdapter.notifyDataSetChanged();
                    if(bean.getData() ==null ||bean.getData().size()<10){
                        mSmart.finishLoadMoreWithNoMoreData();
                    }else{
                        mSmart.setNoMoreData(false);
                    }
                } else {
                    mSmart.finishLoadMoreWithNoMoreData();
                    ToastShow(bean.getMsg());
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

    private void upReadMsg() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", mUser_id);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().upMessageReadStatusByUserId(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                LoginBean bean = new Gson().fromJson(result.toString(), LoginBean.class);
                if (bean.getCode() == 200) {
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsgt(EventMessage msg) {
        if (msg.getMessage().equals("msg")) {
            mSmart.setEnableLoadMore(true);
            pageNum = 1;
            getData();
        }
    }

}
