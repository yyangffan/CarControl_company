package com.hncd.carcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hncd.carcontrol.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemCheckAdapter extends RecyclerView.Adapter<ItemCheckAdapter.ViewHolder> {
    private Context mContext;
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private List<LocalMedia> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private onAddPicClickListener mOnAddPicClickListener;

    public ItemCheckAdapter(Context context, List<LocalMedia> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnAddPicClickListener(onAddPicClickListener onAddPicClickListener) {
        mOnAddPicClickListener = onAddPicClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_check_imgv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImage.setImageResource(R.drawable.ic_add_image);
            viewHolder.mImage.setOnClickListener(v -> mOnAddPicClickListener.onAddPicClick());
        } else {
            LocalMedia bean = mLists.get(position);
            RoundedCorners roundedCorners = new RoundedCorners(8);
            RequestOptions options = new RequestOptions().transform(new CenterCrop(), roundedCorners).placeholder(R.drawable.jy_default).error(R.drawable.jy_default);
            Glide.with(mContext).load(bean.getPath()).apply(options).into(viewHolder.mImage);
            viewHolder.mItemCheckTitle.setText(bean.getTitle());
            //itemView 的点击事件
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    mOnItemClickListener.onItemClickListener(position);
                });
            }
        }



    }
    public List<LocalMedia> getData() {
        return mLists == null ? new ArrayList<>() : mLists;
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 1 :  mLists.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    private boolean isShowAddItem(int position) {
        int size = mLists.size();
        return position == size;
    }


    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

    /**
     * 点击添加图片跳转
     */
    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_check_photo)
        ImageView mImage;
        @BindView(R.id.item_check_title)
        TextView mItemCheckTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
