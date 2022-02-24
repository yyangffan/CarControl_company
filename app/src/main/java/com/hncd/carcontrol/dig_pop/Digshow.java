package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hncd.carcontrol.R;
import com.luck.picture.lib.photoview.PhotoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

public class Digshow extends Dialog {

    private Context mContext;
    private String path = "";
    private PhotoView mPhotoView;

    public Digshow(@NonNull Context context, String pa) {
        super(context);
        mContext = context;
        path = pa;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dig_showimgv);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.app_color_transparent);
        mPhotoView = findViewById(R.id.dig_show_photo);
        initViews();
    }

    private void initViews() {
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_pic).error(R.drawable.default_pic);
        Glide.with(mContext).load(path).apply(options).into(mPhotoView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
