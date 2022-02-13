package com.hncd.carcontrol.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.bean.CheckAllBean;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TadPopAdapter extends RecyclerView.Adapter<TadPopAdapter.ViewHolder> {
    private Context mContext;
    private List<CheckAllBean.DataBean.CheckLineBean> mLists;
    private String now_txt = "";
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private View clickView;

    public TadPopAdapter(Context context,  List<CheckAllBean.DataBean.CheckLineBean> stringList, String now_t) {
        mContext = context;
        mLists = stringList;
        now_txt = now_t;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_td, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        CheckAllBean.DataBean.CheckLineBean bean = mLists.get(position);
        vh.itemView.setTag(bean);
        String name = bean.getLineNo();
        vh.mItemZizhiName.setText(name);
        if (name.equals(now_txt)) {
            clickView = vh.itemView;
            vh.mItemZizhiName.setTextColor(Color.parseColor("#3A7AFD"));
        }else{
            vh.mItemZizhiName.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == clickView) {
                    return;
                }
                TextView tv_new = view.findViewById(R.id.item_zizhi_name);
                tv_new.setTextColor(mContext.getResources().getColor(R.color.appColor));
                if (clickView != null) {
                    CheckAllBean.DataBean.CheckLineBean bean_old = (CheckAllBean.DataBean.CheckLineBean) clickView.getTag();
//                    bean_old.put("pos", -1);
                    TextView tv_old = view.findViewById(R.id.item_zizhi_name);
                    tv_old.setTextColor(mContext.getResources().getColor(R.color.black));
                }
                clickView = view;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(position);
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
        @BindView(R.id.item_zizhi_name)
        TextView mItemZizhiName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
