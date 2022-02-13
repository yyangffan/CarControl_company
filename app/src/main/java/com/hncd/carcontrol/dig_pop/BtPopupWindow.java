package com.hncd.carcontrol.dig_pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hncd.carcontrol.R;


public class BtPopupWindow extends PopupWindow {
    private View mPopView;
    private TextView mtv_ok,mtv_nogaiz,mtv_recover;
    private OnItemClickListener mOnItemClickListener;

    public BtPopupWindow(Context context) {
        super(context);
        initView(context);
        setPopupWindow();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.dialog_bt, null);
        mtv_ok = mPopView.findViewById(R.id.dig_ok);
        mtv_nogaiz = mPopView.findViewById(R.id.dig_nogaiz);
        mtv_recover = mPopView.findViewById(R.id.dig_recover);
        mtv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onOkClickListener();
            }
        });
        mtv_nogaiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onNogazClickListener();
            }
        });
        mtv_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onRecoverClickListener();
            }
        });


    }
    private void setPopupWindow() {
       setContentView(mPopView);// 设置View
       setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的宽
       setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
       setFocusable(true);// 设置弹出窗口可
       setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
    }

    public interface OnItemClickListener{
        void onOkClickListener();
        void onNogazClickListener();
        void onRecoverClickListener();
    }

}
