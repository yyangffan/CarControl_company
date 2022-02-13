package com.luck.picture.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.luck.picture.lib.R;
import com.luck.picture.lib.adapter.DigProjAdapter;
import com.luck.picture.lib.adapter.ItemRecyDecoration;
import com.luck.picture.lib.bean.CheckItemPhotoBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DigPhopro extends Dialog {
    private Context mContext;
    private int select_pos = 0;
    private List<CheckItemPhotoBean> mList;
    private DigProjAdapter mDigProjAdapter;
    private RecyclerView mrecy;
    private LinearLayout mLinearLayout;
    private OnItemClickListener mOnItemClickListener;
    private boolean show_notify;

    public DigPhopro(@NonNull Context context,  int select_pos, List<CheckItemPhotoBean> list) {
        super(context);
        mContext = context;
        this.select_pos = select_pos;
        mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dig_photo_proj);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.picture_color_transparent);
        setCanceledOnTouchOutside(true);
        initViews();
    }

    private void initViews() {
        mrecy = findViewById(R.id.dig_photo_recy);
        mLinearLayout = findViewById(R.id.dig_photo_linear);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mrecy.setLayoutManager(layoutManager);
        mDigProjAdapter = new DigProjAdapter(mContext,mList,select_pos);
        mrecy.addItemDecoration(new ItemRecyDecoration(mContext,LinearLayoutManager.VERTICAL));
        mrecy.setAdapter(mDigProjAdapter);
        mDigProjAdapter.setOnItemClickListener(new DigProjAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                if(pos == select_pos){
                    return;
                }
                CheckItemPhotoBean checkItemPhotoBean = mList.get(pos);
                String title = checkItemPhotoBean.getCheckItemCode()+":"+checkItemPhotoBean.getCheckItemName();
                mDigProjAdapter.setSelect_pos(pos);
                mDigProjAdapter.notifyItemChanged(select_pos);
                select_pos =pos;
                mDigProjAdapter.notifyItemChanged(select_pos);
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemclickListener(pos,title);
                }
                dismiss();
            }
        });
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public void setSelect_pos(int sel_pos) {
        show_notify = true;
        this.select_pos = sel_pos;
    }

    @Override
    public void show() {
        super.show();
        if(show_notify) {
            mDigProjAdapter.setSelect_pos(select_pos);
            mDigProjAdapter.notifyDataSetChanged();
            show_notify =false;
        }

    }

    public interface OnItemClickListener{
        void onItemclickListener(int pos,String title);
    }

}
