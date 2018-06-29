package com.example.florian.myapplication.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;

import java.util.ArrayList;

public class ReleveStocker extends StockCheckedItems<Releve,HistoryDao> {
    public ReleveStocker(Context context) {
        super(new ArrayList<Releve>(), new HistoryDao(context));
    }

    public void exportReleve(){

    }
}
