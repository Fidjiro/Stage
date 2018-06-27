package com.example.florian.myapplication.Activities.Historiques.Releves.InfoRelevesPopups;

import android.os.Bundle;
import android.widget.TextView;

import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;

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
        length.setText(Utils.dfLength.format(rel.getLength()) + "");
    }
}
