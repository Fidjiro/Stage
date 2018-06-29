package com.example.florian.myapplication.Database;

public interface DAO<T extends DatabaseItem> {

    long delete(T item);

    void open();

    void close();
}
