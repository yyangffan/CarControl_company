package com.hncd.carcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.bean.MessageListBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<MessageListBean.DataBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private SimpleDateFormat mSimpleDateFormat;

    public MessageAdapter(Context context, List<MessageListBean.DataBean> stringList) {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_msg, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        MessageListBean.DataBean bean = mLists.get(position);
        vh.mItemMsgTitle.setText(bean.getTitile());
        vh.mItemMsgDate.setText(mSimpleDateFormat.format(new Date(bean.getCreatetime())));
        vh.mItemMsgContent.setText(bean.getContent());

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder   extends RecyclerView.ViewHolder{
        @BindView(R.id.item_msg_title)
        TextView mItemMsgTitle;
        @BindView(R.id.item_msg_date)
        TextView mItemMsgDate;
        @BindView(R.id.item_msg_content)
        TextView mItemMsgContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
