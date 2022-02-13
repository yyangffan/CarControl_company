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

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePhotoAdapter extends RecyclerView.Adapter<ImagePhotoAdapter.ViewHolder> {
    private Context mContext;
    private List<LocalMedia> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public ImagePhotoAdapter(Context context, List<LocalMedia> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_photo, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        LocalMedia bean = mLists.get(position);
        RoundedCorners roundedCorners = new RoundedCorners(8);
        RequestOptions options =new RequestOptions().transform( new CenterCrop(),roundedCorners).placeholder(R.drawable.jy_default).error(R.drawable.jy_default);
        Glide.with(mContext).load(bean.getPath()).apply(options).into(vh.mImage);
        vh.mBannerTitle.setText(bean.getTitle());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
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

    static class ViewHolder   extends RecyclerView.ViewHolder{
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.bannerTitle)
        TextView mBannerTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
