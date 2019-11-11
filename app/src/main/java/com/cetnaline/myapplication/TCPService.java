package com.cetnaline.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPService extends Service {

    private boolean isServiceDestroyed = false;
    private String[] mDefaultMessages = new String[]{
            "你好，我是机器人",
            "请问有什么可以帮助您？",
            "亲，这边建议你退货呢~",
            "要抱抱"
    };

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!isServiceDestroyed) {
                try {
                    final Socket client = serverSocket.accept();
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
                                out.println("欢迎来到聊天室");
                                while (!isServiceDestroyed) {
                                    String str = in.readLine();
                                    System.out.println("msg from client:"+str);
                                    if (str == null) {
                                        break;
                                    }
                                    int i = new Random().nextInt(mDefaultMessages.length);
                                    String msg = mDefaultMessages[i];
                                    out.println(msg);
                                    System.out.println("send:"+msg);
                                }
                                System.out.println("client quit.");
                                in.close();
                                out.close();
                                client.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }











}
