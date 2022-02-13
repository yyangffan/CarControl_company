package com.hncd.carcontrol.ui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import com.hncd.carcontrol.R;

import androidx.annotation.Nullable;


public class FrontService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @TargetApi(26)
    private void setForeground() {
        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel (getString(R.string.app_name),getString(R.string.app_name),NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Intent nfIntent = new Intent(this, MainActivity.class);
        Notification notification=new Notification.Builder (this,getString(R.string.app_name))
                .setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle (getString(R.string.app_name))
                .setSmallIcon (R.mipmap.ic_launcher)
                .setContentText ("请保持程序在后台运行")
                .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
                .build ();
        startForeground (0x111,notification);
        toConnectSocket();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//         在API11之后构建Notification的方式
       /* Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("小二生活商家版") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("请保持程序在后台运行") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
//        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(0x111, notification);// 开始前台服务*/
       startForground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForground(){
        if(Build.VERSION.SDK_INT>=26){
            setForeground();
        }else{
//         在API11之后构建Notification的方式
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
            Intent nfIntent = new Intent(this, MainActivity.class);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle(getString(R.string.app_name)) // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("请保持程序在后台运行") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            Notification notification = builder.build(); // 获取构建好的Notification
//        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            startForeground(0x111, notification);// 开始前台服务
            toConnectSocket();

        }
    }

    private void toConnectSocket(){



    }

}
