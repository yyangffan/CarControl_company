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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.utils.CarShareUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;


public class FrontService extends Service {
    private static final String TAG = "FrontService";
    private String mUser_id;

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
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle (getString(R.string.app_name))
                .setSmallIcon (R.mipmap.logo)
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
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("小二生活商家版") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.logo) // 设置状态栏内的小图标
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
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle(getString(R.string.app_name)) // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.logo) // 设置状态栏内的小图标
                    .setContentText("请保持程序在后台运行") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            Notification notification = builder.build(); // 获取构建好的Notification
//        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            startForeground(0x111, notification);// 开始前台服务
            toConnectSocket();

        }
    }

    private void toConnectSocket(){

        mUser_id = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERID, "");
        String connect_url = Constant.BASE_URL+"webSocket/"+mUser_id;
        initSocket(connect_url);

    }


    private WebSocketClient mWebSocketClient;
    private static final long HEART_BEAT_RATE = 10 * 1000;//心跳间隔
    private long sendTime = 0L;

    // 初始化socket
    public void initSocket(String sock_url) {
        if (null == mWebSocketClient) {
            Log.e(TAG, "initSocket: "+sock_url );
            try {
                mWebSocketClient = new WebSocketClient(new URI(sock_url)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.i(TAG, "State_Socket：连接成功-用户状态");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.i(TAG, "State_Socket：返回数据-用户状态" + message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e(TAG, "State_Socket：已关闭-用户状态 code:" + code + " reason:" + reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e(TAG, "State_Socket：连接错误-用户状态" + ex.toString());
                    }
                };
                mWebSocketClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启重连
     */
    private void reconnectWs() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mWebSocketClient.reconnectBlocking();
                    Log.e(TAG, "State_Socket：重新连接中...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stopConnect() {
        if (mWebSocketClient != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("state", "4");//离线
            try {
                mWebSocketClient.send(new Gson().toJson(map));
                mWebSocketClient.close();
                mWebSocketClient = null;
            } catch (Exception e) {
                Log.e(TAG, "run: " + e.toString());
            }

        }
    }



}
