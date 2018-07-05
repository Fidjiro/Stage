package com.example.eden62.GENSMobile.Activities.MapsActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.MyMapView;
import com.example.eden62.GENSMobile.Tools.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polygon;
import org.mapsforge.map.layer.overlay.Polyline;

import java.lang.reflect.Type;
import java.util.List;

public class ShowInvRelActivity extends AppCompatActivity {

    private MyMapView myMap;

    protected Paint paintFill = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.GREEN), 2,
            Style.FILL);
    protected Paint paintStroke = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.BLACK), 2,
            Style.STROKE);
    public static final int PAINT_STROKE = AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());
        setContentView(R.layout.activity_show_rel);

        myMap = new MyMapView((MapView)findViewById(R.id.mapViewOSM));

        Intent intent = getIntent();
        try {
            Releve rel = intent.getParcelableExtra("releve");
            redrawReleve(rel);
        }catch (Exception e){
            Inventaire inv = intent.getParcelableExtra("inv");
            drawPoint(new LatLong(inv.getLatitude(), inv.getLongitude()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMap.exit();
    }

    /**
     * Ajoute le Layer l à la carte
     *
     * @param l Le layer à ajouter
     */
    protected void addLayer(Layer l) {
        myMap.getLayers().add(l);
    }

    // Redessine le relevé sur la carte
    private void redrawReleve(Releve rel){
        Gson gson = new Gson();
        String relType = rel.getType();
        if (relType.equals("Point")) {
            drawPoint(new LatLong(Double.parseDouble(rel.getLatitudes()), Double.parseDouble(rel.getLongitudes())));
        } else {
            Type type = new TypeToken<List<LatLong>>() {
            }.getType();
            String relLatLongsString = rel.getLat_long();
            List<LatLong> relLatLongs = gson.fromJson(relLatLongsString, type);
            if (relType.equals("Ligne")) {
                Polyline polylineRel = new Polyline(Utils.createPaint(
                        PAINT_STROKE,
                        (int) (4 * myMap.getModel().displayModel.getScaleFactor()),
                        Style.STROKE), AndroidGraphicFactory.INSTANCE);
                polylineRel.getLatLongs().addAll(relLatLongs);
                addLayer(polylineRel);
            } else {
                Polygon relPolygon = new Polygon(paintFill, paintStroke, AndroidGraphicFactory.INSTANCE);
                relPolygon.getLatLongs().addAll(relLatLongs);
                addLayer(relPolygon);
            }
            myMap.centerOn(relLatLongs.get(0));
        }
    }

    //Dessine un point sur la carte
    private void drawPoint(LatLong latLong){
        Marker m = Utils.createMarker(ShowInvRelActivity.this, R.drawable.marker_green, latLong);
        addLayer(m);
        myMap.centerOn(latLong);
    }
}
