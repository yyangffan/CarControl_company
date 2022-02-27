package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;


import com.hncd.carcontrol.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteDialog extends Dialog {
    private static final String TAG = "NoteDialog";
    @BindView(R.id.tccamera_note)
    TextView mTccameraNote;
    private boolean isJs = false;
    private String mContent;

    public NoteDialog(@NonNull Context context, String content) {
        super(context,R.style.DialogStyle);
        mContent = content;
    }

    public void setContent(String content) {
        mContent = content;
        if(mTccameraNote!=null){
            mTccameraNote.setText(content);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_note_test);
        setContentView(R.layout.dialog_note);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(R.color.app_color_transparent);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getWindow().setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 前2 个flag设置dialog 显示到状态栏    第三个设置点击dialog以外的蒙层 不抢夺焦点  响应点击事件
        lp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);
        mTccameraNote.setText(mContent);
    }


}
