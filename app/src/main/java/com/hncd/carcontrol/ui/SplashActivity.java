package com.hncd.carcontrol.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.body.ProgressInfo;
import com.ljy.devring.http.support.body.ProgressListener;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goUpdate();
    }

    private void goUpdate(){
        DevRing.httpManager().addRequestListener("http://file.market.xiaomi.com/download/AppStore/07cdb790249014715ab3f47b1bb6477c11948a5ee/com.tyxh.framlive_2.1.1.apk", new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                Log.i(TAG, "onProgress: "+progressInfo.toString());
            }

            @Override
            public void onProgressError(long id, Exception e) {
                Log.e(TAG, "onProgressError: "+e.toString() );
            }
        });
    }


}