package com.hncd.carcontrol.adapter;

import android.content.Context;
import android.text.TextUtils;
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

public class DisStartAdapter extends RecyclerView.Adapter<DisStartAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public DisStartAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_diss_start, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        vh.mItemDissLscode.setText("1241241241" + position);
        vh.mItemDissTd.setText(String.valueOf(position + 1));
        vh.mItemDissSttm.setText("2022-02-23 12:00");
        String edTm = (String) bean.get("edtm");
        if (TextUtils.isEmpty(edTm)) {
            vh.mItemDissFinish.setVisibility(View.VISIBLE);
        } else {
            vh.mItemDissEdtm.setText(edTm);
            vh.mItemDissFinish.setVisibility(View.GONE);
        }
        vh.mItemDissFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClickListener(position);
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
        @BindView(R.id.item_diss_lscode)
        TextView mItemDissLscode;
        @BindView(R.id.item_diss_td)
        TextView mItemDissTd;
        @BindView(R.id.item_diss_sttm)
        TextView mItemDissSttm;
        @BindView(R.id.item_diss_edtm)
        TextView mItemDissEdtm;
        @BindView(R.id.item_diss_fanish)
        TextView mItemDissFinish;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
