package com.example.eden62.GENSMobile.HistoryAdapters;

import android.widget.CheckBox;

import com.example.eden62.GENSMobile.Activities.Historiques.Stocker.StockCheckedItems;

import java.util.ArrayList;

public interface ItemsAdapter<T extends StockCheckedItems> {

    T getCheckedItemsStocker();

    ArrayList<CheckBox> getAllCheckboxes();
}
