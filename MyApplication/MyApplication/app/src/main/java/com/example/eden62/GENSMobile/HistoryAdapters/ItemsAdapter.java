package com.example.eden62.GENSMobile.HistoryAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.eden62.GENSMobile.Activities.Historiques.Stocker.StockCheckedItems;
import com.example.eden62.GENSMobile.Database.DatabaseItem;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemsAdapter<T extends StockCheckedItems,Y extends DatabaseItem> extends ArrayAdapter<Y> {

    protected List<CheckBox> allCheckBoxes;
    protected T checkedItemsStocker;

    public ItemsAdapter(Context context, List<Y> items) {
        super(context, 0, items);
        allCheckBoxes = new ArrayList<>();
    }

    /**
     * Récupère les items coché
     * @return La liste d'items cochés
     */
    public T getCheckedItemsStocker(){
        return checkedItemsStocker;
    }

    /**
     * Récupère toutes les checkboxes présente dans cet adapter
     * @return La liste de checkboxes
     */
    public List<CheckBox> getAllCheckboxes(){
        return allCheckBoxes;
    }
}
