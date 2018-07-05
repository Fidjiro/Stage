package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

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

    public ReleveStocker(Context context, Map exportedReleves) {
        super(new ArrayList<Releve>(), new HistoryDao(context));
        ctx = context;
        convertisseur = new RelToGpx(context,context.getPackageName());
        this.exportedReleves = exportedReleves;
    }

    /**
     * Exporte les relevés selectionnés
     */
    public void exportReleves() {
        for (Releve rel : checkedItems) {
            addReleveIfNotAlreadyExported(rel);
        }
    }

    public void exportReleveAndSendMail(){
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("vnd.android.cursor.dir/email");
        ArrayList<Uri> uris = new ArrayList<>();
        for (Releve rel : checkedItems) {
            addReleveIfNotAlreadyExported(rel);
            uris.add(Uri.fromFile(exportedReleves.get(rel)));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        ctx.startActivity(Intent.createChooser(intent , "Envoyer par..."));
    }

    private void addReleveIfNotAlreadyExported(Releve rel){
        if (!exportedReleves.containsKey(rel))
            exportedReleves.put(rel,convertisseur.export(rel));
    }
}
