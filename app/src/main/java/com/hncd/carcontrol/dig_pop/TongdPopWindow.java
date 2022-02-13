package com.hncd.carcontrol.dig_pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.TadPopAdapter;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.utils.ItemRecyDecoration;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TongdPopWindow extends PopupWindow {
    private Context mContext;
    private View mV;
    private RecyclerView mRecy;
    private List<CheckAllBean.DataBean.CheckLineBean> mLists;
    private String now_tv = "";
    private TadPopAdapter mTadPopAdapter;
    private OnAdapterClickListener mOnAdapterClickListener;

    public TongdPopWindow(Context context, List<CheckAllBean.DataBean.CheckLineBean> lists, String now_t, int width, int height) {
        super(width,200);
        mContext = context;
        mLists = lists;
        now_tv =now_t;
        initView();
        setContentView();
    }

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        mOnAdapterClickListener = onAdapterClickListener;
    }

    private void initView(){
        mV = LayoutInflater.from(mContext).inflate(R.layout.pop_td,null);
        mRecy = mV.findViewById(R.id.pop_zizhi_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecy.setLayoutManager(linearLayoutManager);
        mRecy.addItemDecoration(new ItemRecyDecoration(mContext,LinearLayoutManager.VERTICAL));
        mTadPopAdapter = new TadPopAdapter(mContext,mLists,now_tv);
        mRecy.setAdapter(mTadPopAdapter);
        mTadPopAdapter.setOnItemClickListener(new TadPopAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                CheckAllBean.DataBean.CheckLineBean stringObjectMap = mLists.get(position);
                String name = stringObjectMap.getLineNo();
                if(mOnAdapterClickListener!=null){
                    mOnAdapterClickListener.onAdapterListener(name);
                    dismiss();
                }

            }
        });


    }

    private void setContentView(){
        setContentView(mV);
//        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的宽
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        setFocusable(true);// 设置弹出窗口可
        setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明


    }

    public interface OnAdapterClickListener{
        void onAdapterListener(String name);
    }

}
