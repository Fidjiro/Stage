package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.eden62.GENSMobile.Parser.RelToGpx;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleveStocker extends StockCheckedItems<Releve,HistoryDao> {

    private RelToGpx convertisseur;
    private Map<Releve,File> exportedReleves;
    private Context ctx;

    public ReleveStocker(Context context) {
        super(new ArrayList<Releve>(), new HistoryDao(context));
        ctx = context;
        convertisseur = new RelToGpx(context,context.getPackageName());
        this.exportedReleves = new HashMap<>();
    }

    /**
     * Exporte les relevés selectionnés
     */
    public void exportReleves() {
        for (Releve rel : checkedItems) {
            if (!exportedReleves.containsKey(rel)) {
                exportedReleves.put(rel,convertisseur.export(rel));
            }
        }
    }

    public void exportReleveAndSendMail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");
        for (Releve rel : checkedItems) {
            if (!exportedReleves.containsKey(rel)) {
                exportedReleves.put(rel,convertisseur.export(rel));
            }
            Uri path = Uri.fromFile(exportedReleves.get(rel));
            intent.putExtra(Intent.EXTRA_STREAM,path);
        }
        ctx.startActivity(Intent.createChooser(intent , "Send email..."));
    }
}
