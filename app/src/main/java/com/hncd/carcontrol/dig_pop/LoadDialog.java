package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.hncd.carcontrol.R;

import androidx.annotation.NonNull;

public class LoadDialog extends Dialog {

    public LoadDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawableResource(R.color.app_color_transparent);
        setCanceledOnTouchOutside(false);


    }
}
