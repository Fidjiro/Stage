package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.eden62.GENSMobile.Parser.RelToGpx;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;

import java.util.ArrayList;
import java.util.List;

public class ReleveStocker extends StockCheckedItems<Releve,HistoryDao> {

    private RelToGpx convertisseur;
    private List<Releve> exportedReleves;

    public ReleveStocker(Context context) {
        super(new ArrayList<Releve>(), new HistoryDao(context));
        convertisseur = new RelToGpx(context,context.getPackageName());
        this.exportedReleves = new ArrayList<>();
    }

    /**
     * Exporte les relevés selectionnés
     */
    public void exportReleves() {
        for (Releve rel : checkedItems) {
            if (!exportedReleves.contains(rel)) {
                convertisseur.export(rel);
                exportedReleves.add(rel);
            }
        }
    }
}
