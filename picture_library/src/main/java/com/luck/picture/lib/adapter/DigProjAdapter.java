package com.luck.picture.lib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luck.picture.lib.R;
import com.luck.picture.lib.bean.CheckItemPhotoBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DigProjAdapter extends RecyclerView.Adapter<DigProjAdapter.ViewHolder>{
    private Context mContext;
    private List<CheckItemPhotoBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private int select_pos = 0;

    public DigProjAdapter(Context context, List<CheckItemPhotoBean> stringList,int sepos) {
        mContext = context;
        mLists = stringList;
        select_pos =sepos;
        mInflater= LayoutInflater.from(mContext);
    }

    public void setSelect_pos(int select_pos) {
        this.select_pos = select_pos;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_dig_proj,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        CheckItemPhotoBean bean=mLists.get(position);
        boolean hasTake = bean.isHasTake();
        String title = bean.getCheckItemCode()+":"+bean.getCheckItemName();
        vh.mtv_title.setText(title);
        vh.mtv_title.setTextColor(Color.parseColor(hasTake?"#B6B6B6":"#000000"));
        if(position == select_pos){
            vh.mtv_title.setTextColor(Color.parseColor("#D21216"));
        }

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
        return mLists==null?0:mLists.size();
    }

    public interface OnItemClickListener{
        void onItemClickListener(int pos);
    }
    public class ViewHolder   extends RecyclerView.ViewHolder{
       private TextView mtv_title;

       public ViewHolder(@NonNull View itemView) {
          super(itemView);
          mtv_title = itemView.findViewById(R.id.item_digproj_title);

       }
    }

}
