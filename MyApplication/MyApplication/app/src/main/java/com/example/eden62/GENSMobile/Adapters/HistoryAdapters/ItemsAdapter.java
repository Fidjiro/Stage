package com.example.eden62.GENSMobile.Adapters.HistoryAdapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.eden62.GENSMobile.Stocker.StockCheckedItems;
import com.example.eden62.GENSMobile.Database.DatabaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter pour les listView des historiques
 *
 * @param <T> L'objet qui stock tous les items cochés
 * @param <Y> Le type d'objet qui sera dans cet adapter
 */
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
