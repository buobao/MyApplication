// IOnNewBookArrivedListener.aidl
package com.cetnaline.myapplication;
import com.cetnaline.myapplication.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
