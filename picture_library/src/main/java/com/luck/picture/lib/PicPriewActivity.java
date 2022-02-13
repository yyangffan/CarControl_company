package com.luck.picture.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.photoview.PhotoView;

import java.io.File;

public class PicPriewActivity extends PictureBaseActivity {
    private PhotoView photoView;
    private TextView mtvTitle;


    @Override
    public int getResourceId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_pic_priew;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        Intent intent = getIntent();
        String path =intent.getStringExtra("path");
        String title = intent.getStringExtra("title");
        photoView= findViewById(R.id.pic_priew_photo);
        mtvTitle= findViewById(R.id.picture_title);
        mtvTitle.setText(title);
//        Glide.with(this).load(path).into(photoView);
        PictureSelectionConfig.imageEngine.loadImage(this, path, photoView);
        findViewById(R.id.left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}