package com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.MapsActivities.ShowInvRelActivity;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.Parser.RelToGpx;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.LoadingMapDialog;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Pop ups d'informations sur un relevé réalisé
 */
public abstract class ReleveInfoPopup extends AppCompatActivity {

    protected TextView nom, type, date, heure;
    protected ImageButton delete;
    protected Button redraw,export;

    protected HistoryDao dao;

    protected Releve rel;

    public static LoadingMapDialog lmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        dao = new HistoryDao(this);
        dao.open();

        lmd = new LoadingMapDialog(this);

        Intent intent = getIntent();
        rel = intent.getParcelableExtra("releve");

        initView();
        setViewsContent();
    }

    /**
     * Rempli les champs avec les informations du relevé consulté via l'historique
     */
    protected void setViewsContent(){
        nom.setText(rel.getNom());
        type.setText(rel.getType());
        date.setText(Utils.printDateWithYearIn2Digit(rel.getDate()));
        heure.setText(rel.getHeure());
    }

    /**
     * Affecte le bon layout en fonction du type de relevé
     */
    protected abstract void setContentView();

    /**
     * Initialise les Views de cette activité
     */
    protected void initView(){
        nom = (TextView) findViewById(R.id.nom);
        type = (TextView) findViewById(R.id.type);
        date = (TextView) findViewById(R.id.date);
        heure = (TextView) findViewById(R.id.heure);

        delete = (ImageButton) findViewById(R.id.deleteRel);
        redraw = (Button) findViewById(R.id.redraw);
        export = (Button) findViewById(R.id.exportRel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });

        redraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lmd.show(true);
                Intent intent = new Intent(ReleveInfoPopup.this,ShowInvRelActivity.class);
                intent.putExtra("releve",rel);
                startActivity(intent);
        }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RelToGpx convertisseur = new RelToGpx(ReleveInfoPopup.this,ReleveInfoPopup.this.getPackageName());
                final Snackbar exportSnackbar = Snackbar.make(export,"Exportation en cours",Snackbar.LENGTH_INDEFINITE);

                AlertDialog.Builder builder = new AlertDialog.Builder(ReleveInfoPopup.this);
                builder.setMessage("stockageInterne/Android/data/" + ReleveInfoPopup.this.getPackageName() + "/files/\n \nL'envoyer par mail ?");
                builder.setTitle("Localisation des fichiers");
                final List<Releve> releveToExport = new ArrayList<>();
                releveToExport.add(rel);
                builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportSnackbar.show();
                        sendFileByMail(convertisseur.exportReleves(releveToExport,rel.getNom()));
                        exportSnackbar.dismiss();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        exportSnackbar.show();
                        convertisseur.exportReleves(releveToExport,rel.getNom());
                        exportSnackbar.dismiss();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    /**
     * Permet d'ajoute le File en paramètre en pièce jointe d'un mail
     *
     * @param file La file qui servira de pièce jointe
     */
    public void sendFileByMail(File file){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");
        Uri uri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        this.startActivity(Intent.createChooser(intent , "Envoyer par..."));
    }

    /**
     * Dialog d'avertissement de suppression de relevé
     *
     * @return Le dialog d'avertissement
     */
    public Dialog createDialog() {
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.avertissementDelete);
        builder.setTitle(getString(R.string.avertissement));
        builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.delete(rel);
                        finishPopUp();
                    }
                });
        builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        box = builder.create();
        return box;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    /**
     * Permet de quitter cete activité
     */
    protected abstract void finishPopUp();
}
