package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hncd.carcontrol.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogoutDialog extends Dialog {


    @BindView(R.id.dig_logout_sure)
    TextView mDigLogoutSure;
    @BindView(R.id.dig_logout_cancel)
    TextView mDigLogoutCancel;
    private OnLogoutClickListener mOnLogoutClickListener;
    private Window mWindow;

    public LogoutDialog(@NonNull Context context) {
        super(context);


    }

    public void setOnLogoutClickListener(OnLogoutClickListener onLogoutClickListener) {
        mOnLogoutClickListener = onLogoutClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_logout);
        ButterKnife.bind(this);
        mWindow = getWindow();
        mWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setBackgroundDrawableResource(R.color.picture_color_transparent);


    }

    @OnClick({R.id.dig_logout_sure, R.id.dig_logout_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_logout_sure:
                if(mOnLogoutClickListener!=null)
                    mOnLogoutClickListener.onLogoutClickListener();
                break;
            case R.id.dig_logout_cancel:
                dismiss();
                break;
        }
    }
    public interface OnLogoutClickListener{
        void onLogoutClickListener();
    }
}
