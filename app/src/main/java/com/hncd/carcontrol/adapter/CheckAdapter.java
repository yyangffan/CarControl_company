package com.hncd.carcontrol.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.bean.RegistInforBean;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {
    private Context mContext;
    private List<RegistInforBean.DataBean.RegInfoBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CheckAdapter(Context context, List<RegistInforBean.DataBean.RegInfoBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recy_check, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        RegistInforBean.DataBean.RegInfoBean bean = mLists.get(position);
        String title = bean.getName();
        String content = bean.getValue();
        vh.mItemCheckTitle.setText(title);
        vh.mItemCheckContent.setText(TextUtils.isEmpty(content)?"":content);

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_check_title)
        TextView mItemCheckTitle;
        @BindView(R.id.item_check_content)
        TextView mItemCheckContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
