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
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.Constant;
import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.utils.CarShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
        EventBus.getDefault().register(this);

    }

    @TargetApi(26)
    private void setForeground() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        Intent nfIntent = new Intent(this, MainActivity.class);
        Notification notification = new Notification.Builder(this, getString(R.string.app_name))
                .setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.logo)
                .setContentText("请保持程序在后台运行")
                .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
                .build();
        startForeground(0x111, notification);
        toConnectSocket();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForground() {
        if (Build.VERSION.SDK_INT >= 26) {
            setForeground();
        } else {
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
            Intent nfIntent = new Intent(this, MainActivity.class);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle(getString(R.string.app_name)) // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.logo) // 设置状态栏内的小图标
                    .setContentText("请保持程序在后台运行") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            Notification notification = builder.build(); // 获取构建好的Notification
            startForeground(0x111, notification);// 开始前台服务
            toConnectSocket();

        }
    }

    private void toConnectSocket() {
        mUser_id = (String) CarShareUtil.getInstance().get(CarShareUtil.APP_USERID, "");
        initSocket();

    }


    private WebSocketClient mWebSocketClient;
    private static final long HEART_BEAT_RATE = 30 * 1000;//心跳间隔
    private long sendTime = 0L;

    public void initSocket() {
        if (null == mWebSocketClient) {
            String sock_url = Constant.BASE_URL + "webSocket/" + mUser_id;
            Log.e(TAG, "initSocket: " + sock_url);
            try {
                mWebSocketClient = new WebSocketClient(new URI(sock_url)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.i(TAG, "State_Socket：连接成功-用户状态");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.i(TAG, "State_Socket：返回数据-" + message);
                        if(!TextUtils.isEmpty(message)){
                            message = message.replaceAll("客户端：","");
                            message = message.replaceAll(",已收到","");
                            JSONObject bean = (JSONObject) JSONObject.parse(message);
                            String content = bean.getString("content");
                            if(!TextUtils.isEmpty(content)){
                                EventMessage event_msg = new EventMessage("msg");
                                event_msg.setContent(content);
                                EventBus.getDefault().post(event_msg);
                            }
                        }

                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e(TAG, "State_Socket：已关闭- code:" + code + " reason:" + reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e(TAG, "State_Socket：连接错误-" + ex.toString());
                    }
                };
                mWebSocketClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
                toStartHeart();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                toStartHeart();
            }
            toStartHeart();
        }
    }


    private void toStartHeart() {
        Log.i(TAG, "initSocket: 启动心跳-用户状态");
        mHandler_socket.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

    private Handler mHandler_socket = new Handler();
    Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if (mWebSocketClient != null) {//长连接已断开
                    if (mWebSocketClient.isClosed()) {
                        reconnectWs();
                    }
                    Log.e(TAG, "run: 发送心跳");
                } else {//长连接处于连接状态
                    initSocket();
                }
                sendTime = System.currentTimeMillis();

            }
            mHandler_socket.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    };

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
            try {
                mWebSocketClient.close();
                mWebSocketClient = null;
                Log.e(TAG, "stopConnect: 链接已断开");
            } catch (Exception e) {
                Log.e(TAG, "run: " + e.toString());
            }
        }
        mHandler_socket.removeCallbacks(heartBeatRunnable);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMsgt(EventMessage msg) {
        if(msg.getMessage().equals("diss")){
            stopConnect();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
