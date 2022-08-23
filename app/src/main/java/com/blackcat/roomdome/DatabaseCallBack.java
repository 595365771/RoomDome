package com.blackcat.roomdome;


public interface DatabaseCallBack<T> {
    void onSuccess( T t);

    void onError( Throwable e);
}
