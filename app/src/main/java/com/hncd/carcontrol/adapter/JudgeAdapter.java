package com.hncd.carcontrol.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JudgeAdapter extends RecyclerView.Adapter<JudgeAdapter.ViewHolder> {
    private Context mContext;
    private List<CheckAllBean.DataBean.CheckItemBean> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public JudgeAdapter(Context context, List<CheckAllBean.DataBean.CheckItemBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recy_judge, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        CheckAllBean.DataBean.CheckItemBean bean = mLists.get(position);
        vh.mItemJudgeEdtRea.setTag(bean);
        vh.mItemJudgeEdtRea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                CheckAllBean.DataBean.CheckItemBean b = (CheckAllBean.DataBean.CheckItemBean)vh.mItemJudgeEdtRea.getTag();
                b.setReason(vh.mItemJudgeEdtRea.getText().toString());

            }
        });

        vh.mItemJudgePos.setText(String.valueOf(position + 1));
        vh.mItemJudgeTitle.setText(bean.getCheckItemName());
        vh.mItemJudgeContent.setText(TextUtils.isEmpty(bean.getCheckItemCode()) ? "" : bean.getCheckItemCode());
        vh.mItemJudgeGroup.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        vh.mItemJudgeRecy.setLayoutManager(linearLayoutManager);
//        vh.mItemJudgeRecy.addItemDecoration(new GridSpacingItemDecoration(4, ScreenUtils.dip2px(mContext, 8), false));加上这句话距离会在更新后继续累加。。。
        ItemCheckAdapter itemCheckAdapter = new ItemCheckAdapter(mContext,bean.getPicLists());
        itemCheckAdapter.setOnAddPicClickListener(new ItemCheckAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onPicAddListener(position);
                }
            }
        });
        vh.mItemJudgeRecy.setAdapter(itemCheckAdapter);
        itemCheckAdapter.setOnItemClickListener(new ItemCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                List<LocalMedia> selectList = itemCheckAdapter.getData();
                shwoLook(selectList, pos);
            }
        });

        int type = bean.getType();
        if (type == 0) {
            int typeone = bean.getTypeone();
            vh.mItemJudgeCheck.setText(typeone == 0 ? "未判定" : (typeone == 3 ? "不合格" : "合格"));
            vh.mItemJudgeCheck.setTextColor(mContext.getResources().getColor(typeone == 0 ? R.color.picture_color_light_grey : R.color.white));
            vh.mItemJudgeCheck.setBackgroundResource(typeone == 0 ? R.drawable.bg_circle_gray : (typeone == 1 ? R.drawable.bg_appcolor : R.drawable.bg_circle_red));
            vh.mItemJudgeGroup.setVisibility(typeone==3?View.VISIBLE:View.GONE);
            vh.mItemJudgeEdtRea.setText(typeone==3?bean.getReason():"");
        } else if (type == 1) {
            int typetwo = bean.getTypetwo();
            vh.mItemJudgeCheck.setText(typetwo == 0 ? "未判定" : (typetwo == 3 ? "不合格" : (typetwo == 1?"合格(未改装)":"合格(改装)")));
            vh.mItemJudgeCheck.setTextColor(mContext.getResources().getColor(typetwo == 0 ? R.color.picture_color_light_grey : (typetwo == 2?R.color.red:R.color.white)));
            vh.mItemJudgeCheck.setBackgroundResource(typetwo == 0 ? R.drawable.bg_circle_gray : (typetwo == 1 ? R.drawable.bg_appcolor : (typetwo == 2?R.drawable.bg_yel:R.drawable.bg_circle_red)));
            vh.mItemJudgeGroup.setVisibility(typetwo==3?View.VISIBLE:View.GONE);
            vh.mItemJudgeEdtRea.setText(typetwo==3?bean.getReason():"");
        }
        vh.mItemJudgeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
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

        void onPicAddListener(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_judge_pos)
        TextView mItemJudgePos;
        @BindView(R.id.item_judge_title)
        TextView mItemJudgeTitle;
        @BindView(R.id.item_judge_content)
        TextView mItemJudgeContent;
        @BindView(R.id.item_judge_check)
        TextView mItemJudgeCheck;
        @BindView(R.id.item_judge_edtrea)
        EditText mItemJudgeEdtRea;
        @BindView(R.id.item_judge_recy)
        RecyclerView mItemJudgeRecy;
        @BindView(R.id.item_judge_group)
        Group mItemJudgeGroup;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void shwoLook(List<LocalMedia> selectList, int position) {
        if (selectList.size() > 0) {
            LocalMedia media = selectList.get(position);
            String mimeType = media.getMimeType();
            int mediaType = PictureMimeType.getMimeType(mimeType);
            PictureSelector.create((Activity) mContext)
                    .themeStyle(R.style.picture_default_style) // xml设置主题
                    //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                    .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                    //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .openExternalPreview(position, selectList);
        }
    }


}
