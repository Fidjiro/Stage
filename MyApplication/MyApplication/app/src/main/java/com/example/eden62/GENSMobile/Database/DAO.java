package com.example.eden62.GENSMobile.Database;

public interface DAO<T extends DatabaseItem> {

    long delete(T item);

    void open();

    void close();
}
