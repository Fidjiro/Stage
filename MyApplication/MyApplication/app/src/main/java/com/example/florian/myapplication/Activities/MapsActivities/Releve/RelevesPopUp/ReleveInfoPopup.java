package com.example.florian.myapplication.Activities.MapsActivities.Releve.RelevesPopUp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.MapsActivities.Releve.MainActivityRel;
import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;

public abstract class ReleveInfoPopup extends AppCompatActivity {

    protected TextView nom, type, date, heure;
    protected ImageButton delete;
    protected Button redraw;

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

    protected void setViewsContent(){
        nom.setText(rel.getNom());
        type.setText(rel.getType());
        date.setText(rel.getDate());
        heure.setText(rel.getHeure());
    }

    protected abstract void setContentView();

    protected void initView(){
        nom = (TextView) findViewById(R.id.nom);
        type = (TextView) findViewById(R.id.type);
        date = (TextView) findViewById(R.id.date);
        heure = (TextView) findViewById(R.id.heure);

        delete = (ImageButton) findViewById(R.id.deleteRel);
        redraw = (Button) findViewById(R.id.redraw);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.deleteReleve(rel);
                finishPopUp();
            }
        });

        redraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReleveInfoPopup.this,MainActivityRel.class);
                intent.putExtra("releve",rel);
                startActivity(intent);
                finishPopUp();
        }
        });
    }

    protected abstract void finishPopUp();
}
