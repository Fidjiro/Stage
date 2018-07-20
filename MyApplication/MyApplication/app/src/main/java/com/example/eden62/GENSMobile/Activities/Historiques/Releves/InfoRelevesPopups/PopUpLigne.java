package com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups;

import android.os.Bundle;
import android.widget.TextView;

import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

/**
 * Pop up d'information sur une ligne
 */
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
