package com.cetnaline.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

public class HorizontalScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_scroll);
//        HorizontalScrollViewEx horizontalScrollView = findViewById(R.id.viewpager);

        ListView listView1 = findViewById(R.id.list1);
        ListView listView2 = findViewById(R.id.list2);
        ListView listView3 = findViewById(R.id.list3);
        ListView listView4 = findViewById(R.id.list4);
        ListView listView5 = findViewById(R.id.list5);

        ArrayList<String> items = new ArrayList<>();
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("DDDDDDDDDDDDD");
        items.add("DDDDDDDDDDDDD");
        items.add("DDDDDDDDDDDDD");
        items.add("DDDDDDDDDDDDD");
        items.add("DDDDDDDDDDDDD");
        items.add("DDDDDDDDDDDDD");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("BBBBBBBBBBBBBB");
        items.add("BBBBBBBBBBBBBB");
        items.add("BBBBBBBBBBBBBB");
        items.add("BBBBBBBBBBBBBB");
        items.add("BBBBBBBBBBBBBB");
        items.add("BBBBBBBBBBBBBB");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");
        items.add("AAAAAAAAA");

        listView1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items));
        listView2.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items));
        listView3.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items));
        listView4.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items));
        listView5.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items));


        //通知栏
        //1.创建Notification
        Notification.Builder builder = new Notification.Builder(this, "buobao.myapplication");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Hello world");
        builder.setContentTitle("Hello world");
        builder.setContentText("this is notification content.");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent,true);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.msg,"textview for msg.");
        remoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        builder.setCustomContentView(remoteViews);


        //2.为NotificationManager添加一个消息通道
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("buobao.myapplication","buobao.myapplication",NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("buobao.myapplication");
        notificationManager.createNotificationChannel(channel);

        //发送消息到通知栏
        notificationManager.notify(1, builder.build());
    }
}




























