package com.example.florian.myapplication.Activities.MapsActivities.Releve.RelevesPopUp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.florian.myapplication.R;

public class PopUpLigne extends ReleveInfoPopup {

    protected TextView length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pop_up_ligne);
    }

    @Override
    protected void finishPopUp() {
        this.finish();
    }

    @Override
    protected void initView() {
        super.initView();
        length = (TextView) findViewById(R.id.longueur);
    }

    @Override
    protected void setViewsContent() {
        super.setViewsContent();
        length.setText(rel.getLength() + "");
    }
}
