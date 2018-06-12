package com.example.florian.myapplication.Activities.MapsActivities.Releve.RelevesPopUp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.florian.myapplication.R;

public class PopUpPolygone extends ReleveInfoPopup {

    protected TextView perimeter, area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pop_up_polygone);
    }

    @Override
    protected void finishPopUp() {
        this.finish();
    }

    @Override
    protected void initView() {
        super.initView();
        perimeter = (TextView) findViewById(R.id.perimetre);
        area = (TextView) findViewById(R.id.area);
    }

    @Override
    protected void setViewsContent() {
        super.setViewsContent();
        perimeter.setText(rel.getPerimeter() + "");
        area.setText(rel.getArea() + "");
    }
}
