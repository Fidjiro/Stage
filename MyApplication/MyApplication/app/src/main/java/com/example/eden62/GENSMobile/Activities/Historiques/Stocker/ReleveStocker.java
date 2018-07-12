package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.eden62.GENSMobile.Activities.Historiques.Inventaires.HistoryRecensementActivity;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.HistoryReleveActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Parser.RelToGpx;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReleveStocker extends StockCheckedItems<Releve,HistoryDao>{

    private RelToGpx convertisseur;
    private Map<List<Releve>,File> exportedReleves;
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
    public void exportReleves(String gpxName) {
        addRelevesIfNotAlreadyExported(checkedItems, gpxName);
    }

    /**
     * Exporte les relevés selectionnés et les fourni ensuite en pièce jointe d'un mail
     */
    public void exportReleveAndSendMail(String gpxName){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");
        addRelevesIfNotAlreadyExported(checkedItems,gpxName);
        Uri uri = Uri.fromFile(exportedReleves.get(checkedItems));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        ctx.startActivity(Intent.createChooser(intent , "Envoyer par..."));
    }

    //Exporte le relevé puis l'ajoute à la hashmap s'il n'a pas déjà été exporté
    private void addRelevesIfNotAlreadyExported(List<Releve> rels, String name){
        if (!exportedReleves.containsKey(rels))
            exportedReleves.put(rels, convertisseur.exportReleves(rels, name));
    }
}
