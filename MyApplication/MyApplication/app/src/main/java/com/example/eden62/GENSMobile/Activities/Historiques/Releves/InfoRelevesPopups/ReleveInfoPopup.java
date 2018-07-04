package com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.MapsActivities.ShowInvRelActivity;
import com.example.eden62.GENSMobile.Parser.RelToGpx;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

public abstract class ReleveInfoPopup extends AppCompatActivity {

    protected TextView nom, type, date, heure;
    protected ImageButton delete;
    protected Button redraw,export;

    protected HistoryDao dao;

    protected Releve rel;

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
                showProgress(true);
                Intent intent = new Intent(ReleveInfoPopup.this,ShowInvRelActivity.class);
                intent.putExtra("releve",rel);
                startActivity(intent);
                finishPopUp();
        }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelToGpx convertisseur = new RelToGpx(ReleveInfoPopup.this,ReleveInfoPopup.this.getPackageName());
                convertisseur.export(rel);
            }
        });
    }

    /**
     * Dialog d'avertissement de suppression de relevé
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

    /**
     * Affiche un message pour prévenir l'utilisateur du chargement de la carte
     * @param show Si <code>true</code> le message s'affiche, n'efface si <code>false</code>
     */
    protected void showProgress(final boolean show) {

        TextView mProgressView = (TextView) findViewById(R.id.action_progress);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Permet de quitter cete activité
     */
    protected abstract void finishPopUp();
}
