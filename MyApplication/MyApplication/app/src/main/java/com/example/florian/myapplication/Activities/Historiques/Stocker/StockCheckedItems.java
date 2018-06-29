package com.example.florian.myapplication.Activities.Historiques.Stocker;

import com.example.florian.myapplication.Database.DAO;
import com.example.florian.myapplication.Database.DatabaseItem;

import java.util.List;

public abstract class StockCheckedItems<T extends DatabaseItem,Y extends DAO> {

    private List<T> checkedItems;
    private Y dao;

    public StockCheckedItems(List<T> checkedItems, Y dao) {
        this.checkedItems = checkedItems;
        this.dao = dao;
    }

    public void add(T item){
        checkedItems.add(item);
    }

    public void remove(T item){
        checkedItems.remove(item);
    }

    public void deleteCheckedItemsFromDao(){
        dao.open();
        for(T item : checkedItems){
            dao.delete(item);
        }
        dao.close();
    }

}
