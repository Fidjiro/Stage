package com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups;

import android.os.Bundle;
import android.widget.TextView;

import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

/**
 * Pop up d'information sur un polygone
 */
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
        perimeter.setText(Utils.dfLength.format(rel.getPerimeter()) + "");
        area.setText(Utils.dfLength.format(rel.getArea()) + "");
    }
}
