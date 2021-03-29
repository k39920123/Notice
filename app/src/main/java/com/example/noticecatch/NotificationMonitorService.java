package com.example.noticecatch;

import android.app.Notification;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationMonitorService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        String packageName = sbn.getPackageName(); // 取得應用程式包名
        String title = extras.getString(Notification.EXTRA_TITLE); // 取得通知欄標題
        String text = extras.getString(Notification.EXTRA_TEXT); // 取得通知欄文字
        Drawable smallIcon = null;
        try {
            // 取得通知欄的小圖示
            int iconId = extras.getInt(Notification.EXTRA_SMALL_ICON);
            PackageManager manager = getPackageManager();
            Resources resources = manager.getResourcesForApplication(packageName);
            smallIcon = resources.getDrawable(iconId);
        } catch (
                PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap largeIcon = sbn.getNotification().largeIcon; // 取得通知欄的大圖示
        MainActivity.show(packageName, title, text, smallIcon, largeIcon);//傳送資料
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){}

}
