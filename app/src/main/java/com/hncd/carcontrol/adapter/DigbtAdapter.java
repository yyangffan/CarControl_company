package com.hncd.carcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hncd.carcontrol.R;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DigbtAdapter extends RecyclerView.Adapter<DigbtAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private int type;
    private View clickView;


    public DigbtAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dig_bt, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        String title = (String) bean.get("title");
        int this_type = (int) bean.get("type");
        vh.mItemDigTitle.setText(title);
        vh.mItemDigTitle.setTextColor(mContext.getResources().getColor(R.color.black));
        if (this_type == type) {
            vh.mItemDigTitle.setTextColor(mContext.getResources().getColor(R.color.appColor));
            clickView = vh.itemView;
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != clickView) {
                    TextView tv_old = clickView.findViewById(R.id.item_dig_title);
                    tv_old.setTextColor(R.color.black);
                    TextView tv_new = view.findViewById(R.id.item_dig_title);
                    tv_new.setTextColor(R.color.appColor);
                    clickView = view;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClickListener(position);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_dig_title)
        TextView mItemDigTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
