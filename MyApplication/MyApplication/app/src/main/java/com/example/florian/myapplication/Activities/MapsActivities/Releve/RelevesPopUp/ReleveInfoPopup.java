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
    
    protected Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        setCtx();
        
        initView();
    }

    protected abstract Context setCtx();

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

            }
        });

        redraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Releve rel = intent.getParcelableExtra("releve");
                Intent inten = new Intent(ReleveInfoPopup.this,MainActivityRel.class);
                inten.putExtra("releve",rel);
                startActivity(inten);
                finishPopUp();
        }
        });
    }

    protected abstract void finishPopUp();
}
