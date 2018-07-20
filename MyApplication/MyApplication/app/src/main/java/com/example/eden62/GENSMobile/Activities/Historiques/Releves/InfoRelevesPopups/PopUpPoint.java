package com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups;

import android.os.Bundle;
import android.widget.TextView;

import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;
import com.example.eden62.GENSMobile.Tools.XY;

import org.mapsforge.core.model.LatLong;

/**
 * Pop up d'information sur un point
 */
public class PopUpPoint extends ReleveInfoPopup {

    protected TextView posWgs, posL93;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pop_up_point);
    }

    @Override
    protected void finishPopUp() {
        this.finish();
    }

    @Override
    protected void initView() {
        super.initView();
        posWgs = (TextView) findViewById(R.id.posWgs);
        posL93 = (TextView) findViewById(R.id.posL93);
    }

    @Override
    protected void setViewsContent() {
        super.setViewsContent();
        double lat = Double.parseDouble(rel.getLatitudes());
        double lon = Double.parseDouble(rel.getLongitudes());
        XY xy = Utils.convertWgs84ToL93(new LatLong(lat,lon));

        posWgs.setText(Utils.dfPosWgs.format(lat)+ " ; " + Utils.dfPosWgs.format(lon));
        posL93.setText(Utils.dfPosL93.format(xy.x) + " ; " + Utils.dfPosL93.format(xy.y));
    }
}
