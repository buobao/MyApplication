// IBookManager.aidl
package com.cetnaline.myapplication;

import com.cetnaline.myapplication.Book;
import com.cetnaline.myapplication.IOnNewBookArrivedListener;

interface IBookManager {
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}
