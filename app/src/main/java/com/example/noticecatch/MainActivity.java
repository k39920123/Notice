package com.example.noticecatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static TextView textView;
    private static ImageView smallIcon,largeIcon;
    private static Drawable drawableIcon;//儲存通知訊息的應用程式小圖示
    private static Bitmap bitmapIcon;//儲存通知訊息大圖示
    private static String string;//儲存包名、標題、內容文字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        smallIcon = (ImageView) findViewById(R.id.smallIcon);
        largeIcon  = (ImageView) findViewById(R.id.largeIcon);

        if(!isPurview(this)){ // 檢查權限是否開啟，未開啟則開啟對話框
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.app_name)
                    .setMessage("請啟用通知欄擷取權限")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) { // 對話框取消事件
                            finish();
                        }})
                    .setPositiveButton("前往", new DialogInterface.OnClickListener() { // 對話框按鈕事件
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳轉自開啟權限畫面，權限開啟後通知欄擷取服務將自動啟動。
                            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        }}
                    ).show();
        }
    }
    private boolean isPurview(Context context) { // 檢查權限是否開啟 true = 開啟 ，false = 未開啟
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    static void show(String packageName, String title, String text, Drawable small, Bitmap large){

        string = "包名：" + packageName + "\n\n" +
                "標題：" + title + "\n\n" +
                "文字：" + text + "\n\n" ;
        drawableIcon = small;
        bitmapIcon = large;

        new Thread(new Runnable(){
            public void run(){
                Message msg = Message.obtain();
                handler.sendMessage(msg);
            }
        }).start();
    }
    //更新畫面
    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                //將資料顯示，更新至畫面
                textView.setText(string);
                smallIcon.setImageDrawable(drawableIcon);
                largeIcon.setImageBitmap(bitmapIcon);
            }catch (Exception e){}
        }
    };
}