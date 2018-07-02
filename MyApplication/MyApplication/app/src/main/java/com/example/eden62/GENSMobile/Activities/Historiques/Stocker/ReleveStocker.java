package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.eden62.GENSMobile.Activities.RelToGpx;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;

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
