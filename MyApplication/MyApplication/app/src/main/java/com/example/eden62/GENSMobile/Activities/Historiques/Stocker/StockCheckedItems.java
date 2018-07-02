package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import com.example.eden62.GENSMobile.Database.DAO;
import com.example.eden62.GENSMobile.Database.DatabaseItem;

import java.util.List;

public abstract class StockCheckedItems<T extends DatabaseItem,Y extends DAO> {

    protected List<T> checkedItems;
    protected Y dao;

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
