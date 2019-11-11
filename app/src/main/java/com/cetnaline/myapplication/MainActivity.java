package com.cetnaline.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "msg from server:"+msg.getData().getString("msg"));
                    break;
                    default:
                        super.handleMessage(msg);
            }
        }
    }

    private Messenger mService;

    private MessengerHandler messengerHandler = new MessengerHandler();

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = Message.obtain(null, 1);
            Bundle data = new Bundle();
            data.putString("msg","hello service");
            message.setData(data);
            message.replyTo = mGetReplyMessenger;
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection,Context.BIND_AUTO_CREATE);

//        messengerHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent1 = new Intent(MainActivity.this, BookManagerActivity.class);
//                startActivity(intent1);
//            }
//        }, 3000);

        final ImageView img = findViewById(R.id.img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageLevel(3);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent2, 1);
                } else {
                    Button button = new Button(MainActivity.this);
                    button.setText("button");
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, PixelFormat.TRANSPARENT);

                    layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                    layoutParams.x = 100;
                    layoutParams.y = 300;

                    ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).addView(button, layoutParams);


//            ImageView callHead = new ImageView(this);
//            callHead.setImageResource(R.mipmap.ic_launcher);
//
//            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                    PixelFormat.TRANSLUCENT);
//            params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
//            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//            wm.addView(callHead, params);

                }
            }
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
//                    ImageView callHead = new ImageView(this);
//                    callHead.setImageResource(R.mipmap.ic_launcher);
//
//                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                            WindowManager.LayoutParams.WRAP_CONTENT,
//                            WindowManager.LayoutParams.WRAP_CONTENT,
//                            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
//                            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                            PixelFormat.TRANSLUCENT);
//                    params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
//                    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//                    wm.addView(callHead, params);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData cData = ClipData.newPlainText("text", "nmmbdfadasdada");
//            cmb.setPrimaryClip(cData);
            ClipData clipData = cmb.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence text = clipData.getItemAt(0).getText();
                Toast.makeText(this, text.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}


























