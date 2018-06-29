package com.example.florian.myapplication.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.florian.myapplication.Database.CampagneDatabase.CampagneDAO;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;

import java.util.ArrayList;

public class InventoryStocker extends StockCheckedItems<Inventaire,CampagneDAO> {

    public InventoryStocker(Context context) {
        super(new ArrayList<Inventaire>(), new CampagneDAO(context));
    }
}
