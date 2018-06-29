package com.example.florian.myapplication.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.florian.myapplication.Activities.RelToGpx;
import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;

import java.util.ArrayList;

public class ReleveStocker extends StockCheckedItems<Releve,HistoryDao> {

    private RelToGpx convertisseur;

    public ReleveStocker(Context context) {
        super(new ArrayList<Releve>(), new HistoryDao(context));
        convertisseur = new RelToGpx(context,context.getPackageName());
    }

    public void exportReleves(){
        for(Releve rel : checkedItems){
            convertisseur.export(rel);
        }
    }
}
