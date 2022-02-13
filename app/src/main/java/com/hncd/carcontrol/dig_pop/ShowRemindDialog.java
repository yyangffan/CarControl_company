package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hncd.carcontrol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowRemindDialog extends Dialog {
    @BindView(R.id.remind_dialog_left)
    TextView mRemindDialogLeft;
    @BindView(R.id.remind_dialog_right)
    TextView mRemindDialogRight;
    private Context mContext;
    private OnRemindTextClickListener listener;


    public ShowRemindDialog(Context context) {
        super(context);
        mContext = context;
    }

    public ShowRemindDialog setListener(OnRemindTextClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remind);
        ButterKnife.bind(this);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.remind_dialog_left, R.id.remind_dialog_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.remind_dialog_left:
                if (listener != null) {
                    listener.OnLeftTxtClickLisntener();
                }
                dismiss();
                break;
            case R.id.remind_dialog_right:
                if (listener != null) {
                    listener.OnRightTxtClickListener();
                }
                dismiss();
                break;
        }
    }


    /*点击下方按钮的监听*/
    public abstract static class OnRemindTextClickListener {
        public void OnLeftTxtClickLisntener() {
        }

        ;

        public void OnRightTxtClickListener() {
        }

        ;
    }

}

