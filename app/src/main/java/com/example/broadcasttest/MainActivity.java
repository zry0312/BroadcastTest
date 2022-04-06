package com.example.broadcasttest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * 动态注册并使用广播接收器
     */

    private IntentFilter intentFilter;
    private NetChangeReceiver netChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
        // 添加action,当网络情况发生变化时，系统就是发送一条值为android.net.conn.CONNECTIVITY_CHANGE的广播
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetChangeReceiver();
        // 注册广播
        registerReceiver(netChangeReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册，动态注册的广播接收器一定要取消注册才行
        unregisterReceiver(netChangeReceiver);
    }

    // 创建一个名为NetChangeReceiver的广播接收器
    class NetChangeReceiver extends BroadcastReceiver {

        // 当接收到广播，便执行onReceive()方法
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = false;
            //获得网络连接服务
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            //获取wifi连接状态
            NetworkInfo.State state =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            //判断是否正在使用wifi网络
            if (state == NetworkInfo.State.CONNECTED) {
                success = true;
                Log.i("TAG","已连接上WIFI数据");
            }
            //获取GPRS状态
            state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            //判断是否在使用GPRS网络
            if (state == NetworkInfo.State.CONNECTED) {
                success = true;
                Log.i("TAG","已连接上移动数据");
            }
            //如果没有连接成功
            if(!success){
                Toast.makeText(context,"当前网络无连接",Toast.LENGTH_SHORT).show();
                Log.i("TAG","当前网络不可用");
            }
        }
    }


}


