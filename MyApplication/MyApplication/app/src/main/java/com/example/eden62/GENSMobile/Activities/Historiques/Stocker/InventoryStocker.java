package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;

import java.util.ArrayList;

public class InventoryStocker extends StockCheckedItems<Inventaire,CampagneDAO> {

    public InventoryStocker(Context context) {
        super(new ArrayList<Inventaire>(), new CampagneDAO(context));
    }
}
