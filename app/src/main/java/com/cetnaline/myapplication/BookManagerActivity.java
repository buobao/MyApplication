package com.cetnaline.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
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


        Uri uri = Uri.parse("content://com.cetnaline.myapplication.PROVIDER/book");
        ContentValues values = new ContentValues();
        values.put("_id",9);
        values.put("name", "程序设计艺术");
        getContentResolver().insert(uri,values);

        Cursor cursor = getContentResolver().query(uri,new String[]{"_id","name"},null,null,null);
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.setBookId(cursor.getInt(0));
            book.setBookName(cursor.getString(1));
            Log.i(TAG,"query book from contentprovider is:["+book.getBookId()+","+book.getBookName()+"]");
        }


        ContentValues values1 = new ContentValues();
        values1.put("name", "程序设计的艺术？？？");
        String whereClause = "_id=? AND name=?";
        String[] whereArgs = {String.valueOf(9),"程序设计艺术"};
        getContentResolver().update(uri,values1,whereClause,whereArgs);

        Cursor cursor1 = getContentResolver().query(uri,new String[]{"_id","name"},null,null,null);
        while (cursor1.moveToNext()) {
            Book book = new Book();
            book.setBookId(cursor1.getInt(0));
            book.setBookName(cursor1.getString(1));
            Log.i(TAG,"query book from contentprovider is:["+book.getBookId()+","+book.getBookName()+"]");
        }
//        getContentResolver().query(uri,null,null,null,null);
//        getContentResolver().query(uri,null,null,null,null);

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
