package com.cetnaline.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = "BookManagerActivity";

    private TextView textView;

    private IBookManager bookManager;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Book book = (Book) msg.obj;
                    textView.setText(textView.getText()+book.getBookName()+"\n");
                    break;
                    default:
                        super.handleMessage(msg);

            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = bookManager.getBookList();
                String names = "";
                for (Book book : list) {
                    names += book.getBookName()+"\n";
                }
                textView.setText(names);

                bookManager.addBook(new Book(3,"Adding Book"));

                List<Book> newlist = bookManager.getBookList();
                names = "";
                for (Book book : newlist) {
                    names += book.getBookName()+"\n";
                }
                textView.setText(names);

                bookManager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IOnNewBookArrivedListener listener =new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(1,newBook).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        textView = findViewById(R.id.booknames);

        Intent intent = new Intent(this,BookService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            List<Book> list = bookManager.getBookList();
//            String names = "";
//            for (Book book : list) {
//                names += book.getBookName()+"/n";
//            }
//            textView.setText(names);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        if (bookManager != null && bookManager.asBinder().isBinderAlive()) {
            try {
                bookManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(connection);
        super.onDestroy();

    }
}
