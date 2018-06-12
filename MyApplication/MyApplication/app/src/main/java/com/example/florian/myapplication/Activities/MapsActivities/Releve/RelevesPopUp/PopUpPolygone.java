package com.example.florian.myapplication.Activities.MapsActivities.Releve.RelevesPopUp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.florian.myapplication.R;

public class PopUpPolygone extends ReleveInfoPopup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pop_up_polygone);
    }

    @Override
    protected Context setCtx() {
        return this;
    }

    @Override
    protected void finishPopUp() {
        this.finish();
    }
}
