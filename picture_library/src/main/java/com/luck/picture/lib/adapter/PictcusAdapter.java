package com.luck.picture.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictcusAdapter extends RecyclerView.Adapter<PictcusAdapter.ViewHolder> {
    private Context mContext;
    private List<LocalMedia> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public PictcusAdapter(Context context, List<LocalMedia> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull PictcusAdapter.ViewHolder holder, int position) {
        LocalMedia bean = mLists.get(position);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.default_pic).error(R.drawable.default_pic);
//        holder.mitemTxt.setText("0103车辆识别代码：我弄个你问哦"+position);
        Glide.with(mContext).load(bean.getPath()).apply(requestOptions).into(holder.mItemImgv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClickListener(position);
                }
            }
        });

    }

    @NonNull
    @Override
    public PictcusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cus_img, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mItemImgv;
        TextView mitemTxt;

        ViewHolder(View view) {
            super(view);
            mItemImgv = view.findViewById(R.id.item_cus_img);
            mitemTxt = view.findViewById(R.id.item_cus_title);


        }
    }
}
