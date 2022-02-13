package com.hncd.carcontrol.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.CarShareUtil;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChangepwdActivity extends CarBaseActivity {

    @BindView(R.id.forget_next_number)
    EditText mForgetNextNumber;
    @BindView(R.id.forget_next_agin)
    EditText mForgetNextAgin;
    @BindView(R.id.forget_next_sure)
    Button mForgetNextSure;
    private boolean theFirst, theTwice = false;
    private int theFirstLength, theTwiceLength = 0;
    private int pwd_length = 1;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_changepwd;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        initViews();
    }

    @OnClick({R.id.changepwd_back, R.id.forget_next_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changepwd_back:
                finish();
                break;
            case R.id.forget_next_sure:
                isSure();
                break;
        }
    }

    private void isSure() {
        String pwd_one = mForgetNextNumber.getText().toString();
        String pwd_twice = mForgetNextAgin.getText().toString();
        if(TextUtils.isEmpty(pwd_one)||TextUtils.isEmpty(pwd_twice)){
            ToastShow("请输入密码");
            return;
        }
      /*  if (!isLetterDigit(pwd_one)) {
            ToastShow("密码需为6-12位数字字母组合");
            return;
        }*/
        if (!pwd_one.equals(pwd_twice)) {
            ToastShow("两次密码输入不一致");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userName", mUser_name);
        map.put("pwd", pwd_twice);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().upPwd(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                ToastShow(bean.getMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }


    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


    private void initViews() {
        mForgetNextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                theFirst = charSequence.length() >= pwd_length ? true : false;
                theFirstLength = charSequence.length();
                if (theTwice && theFirst) {
                    if (theTwiceLength != 0 && charSequence.length() == theTwiceLength) {
                        mForgetNextSure.setEnabled(true);
                        mForgetNextSure.setBackground(getResources().getDrawable(R.drawable.bg_appcolor));
                    } else {
                        mForgetNextSure.setEnabled(false);
                        mForgetNextSure.setBackground(getResources().getDrawable(R.drawable.bg_circle_graya));
                    }
                } else {
                    mForgetNextSure.setEnabled(false);
                    mForgetNextSure.setBackground(getResources().getDrawable(R.drawable.bg_circle_graya));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mForgetNextAgin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                theTwice = charSequence.length() >= pwd_length ? true : false;
                theTwiceLength = charSequence.length();
                if (theTwice && theFirst) {
                    if (theFirstLength != 0 && charSequence.length() == theFirstLength) {
                        mForgetNextSure.setEnabled(true);
                        mForgetNextSure.setBackground(getResources().getDrawable(R.drawable.bg_appcolor));
                    } else {
                        mForgetNextSure.setEnabled(false);
                        mForgetNextSure.setBackground(getResources().getDrawable(R.drawable.bg_circle_graya));
                    }
                } else {
                    mForgetNextSure.setEnabled(false);
                    mForgetNextSure.setBackground(getResources().getDrawable(R.drawable.bg_circle_graya));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
