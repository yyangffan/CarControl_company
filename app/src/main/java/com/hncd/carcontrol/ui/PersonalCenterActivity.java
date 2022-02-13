package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.LoginBean;
import com.hncd.carcontrol.bean.MessageNoBean;
import com.hncd.carcontrol.dig_pop.LogoutDialog;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.views.CircleImageView;
import com.ljy.devring.DevRing;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PersonalCenterActivity extends CarBaseActivity {

    @BindView(R.id.mine_num)
    TextView mMineNum;
    @BindView(R.id.mine_head)
    CircleImageView mMineHead;
    @BindView(R.id.mine_name)
    TextView mMineName;
    @BindView(R.id.mine_accounts)
    TextView mMineAccounts;
    private LogoutDialog mLogoutDialog;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        mLogoutDialog = new LogoutDialog(this);
        mLogoutDialog.setOnLogoutClickListener(new LogoutDialog.OnLogoutClickListener() {
            @Override
            public void onLogoutClickListener() {
                loginOut();
            }
        });
        getData();
    }

    @OnClick({R.id.mine_back, R.id.mine_ll_msg, R.id.mine_changepwd, R.id.mine_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_back:
                finish();
                break;
            case R.id.mine_ll_msg:
                statActivity(MessageActivity.class);
                break;
            case R.id.mine_changepwd:
                statActivity(ChangepwdActivity.class);
                break;
            case R.id.mine_logout:
                mLogoutDialog.show();
                break;
        }
    }

    private void getData() {
        mMineName.setText(mLoginBean.getData().getName());
        mMineAccounts.setText(mUser_name);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.icon_default).error(R.drawable.icon_default).circleCrop();
        Glide.with(this).load(R.drawable.icon_default).apply(requestOptions).into(mMineHead);
        getMsgNum();
    }

    private void loginOut() {
        CarShareUtil.getInstance().clear();
        startActivity(new Intent(this, LoginActivity.class));
        DevRing.activityListManager().killAllExclude(LoginActivity.class);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMsgNum();

    }

    /*获取未读消息数量*/
    private void getMsgNum(){
        Map<String, Object> map = new HashMap<>();
        map.put("userId", mUser_id);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getNoReadMessageNum(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                MessageNoBean bean = new Gson().fromJson(result.toString(), MessageNoBean.class);
                if (bean.getCode() == 200) {
                    Integer num = bean.getData().getNum();
                    if(num>0){
                        mMineNum.setVisibility(View.VISIBLE);
                        mMineNum.setText(num.toString());
                    }else{
                        mMineNum.setVisibility(View.GONE);
                    }
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
