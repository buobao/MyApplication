package com.cetnaline.myapplication;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

public class MyBookMangerImpl extends Binder implements IMyBookManger {

    public MyBookMangerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IMyBookManger asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }

        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IMyBookManger) {
            return (IMyBookManger) iin;
        }

        return new MyBookMangerImpl.Proxy(obj);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

        switch (code) {
            case INTERFACE_TRANSACTION:{
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList:{
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook:{
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (data.readInt() != 0) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
        }

        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IMyBookManger {
        private IBinder mRemote;

        Proxy(IBinder mRemote) {
            this.mRemote = mRemote;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            java.util.List<com.cetnaline.myapplication.Book> _result;
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(IBookManager.Stub.TRANSACTION_getBookList, _data, _reply, 0);
                _reply.readException();
                _result = _reply.createTypedArrayList(com.cetnaline.myapplication.Book.CREATOR);
            }
            finally {
                _reply.recycle();
                _data.recycle();
            }
            return _result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                if ((book!=null)) {
                    _data.writeInt(1);
                    book.writeToParcel(_data, 0);
                }
                else {
                    _data.writeInt(0);
                }
                mRemote.transact(IBookManager.Stub.TRANSACTION_addBook, _data, _reply, 0);
                _reply.readException();
            }
            finally {
                _reply.recycle();
                _data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }
    }
}
