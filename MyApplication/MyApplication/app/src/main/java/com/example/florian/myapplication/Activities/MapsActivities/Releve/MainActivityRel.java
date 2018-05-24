package com.example.florian.myapplication.Activities.MapsActivities.Releve;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.MapsActivities.MainActivity;
import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Activité carte pour les relevés de terrain
 */
public class MainActivityRel extends MainActivity {

    protected HistoryDao dao;

    protected ImageButton lineButton, polygonButton, pointButton;
    protected EditText nomReleve;
    protected TextView lineLength_perimeterText;
    protected LinearLayout nomReleveForm;
    protected Button validNom, finReleve, mesReleve;

    protected String currentName;

    protected Handler handler;
    protected Runnable pointsTaker;

    protected Polyline polyline;
    protected List<LatLong> latLongsLine;
    protected double lineLength;
    protected List<LatLong> latLongsPolygon;
    protected double polygonPerimeter;
    protected Polygon polygon;

    protected int currentReleve;
    protected final int POLYGON = 2;
    protected final int LINE = 1;
    protected final int NO_RELEVE = 0;

    protected Paint paintFill = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.GREEN), 2,
            Style.FILL);
    protected Paint paintStroke = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.BLACK), 2,
            Style.STROKE);
    public static final int PAINT_STROKE = AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        dao = new HistoryDao(this);
        dao.open();

        lineButton = (ImageButton) findViewById(R.id.bouton_releve_ligne);
        polygonButton = (ImageButton) findViewById(R.id.bouton_releve_polygone);
        pointButton = (ImageButton) findViewById(R.id.bouton_releve_point);
        nomReleve = (EditText) findViewById(R.id.nomReleve);
        nomReleveForm = (LinearLayout) findViewById(R.id.nomReleveLayout);
        validNom = (Button) findViewById(R.id.validerNom);
        finReleve = (Button) findViewById(R.id.finReleve);
        mesReleve = (Button) findViewById(R.id.mesReleves);
        lineLength_perimeterText = (TextView) findViewById(R.id.lineLength_perimeter);

        mesReleve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRel.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener stopListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAvertissementDialog().show();
            }
        };

        View.OnClickListener startLineListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noReleveInProgress()) {
                    setCurrentReleve(LINE);
                    startLine();
                    finReleve.setVisibility(View.VISIBLE);
                }
            }
        };

        View.OnClickListener startPolygonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noReleveInProgress()) {
                    setCurrentReleve(POLYGON);
                    startPolygon();
                    finReleve.setVisibility(View.VISIBLE);
                }
            }
        };

        validNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentName = nomReleve.getText().toString();
                nomReleveForm.setVisibility(View.INVISIBLE);
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nomReleve.getWindowToken(),0);
            }
        });

        lineButton.setOnClickListener(startLineListener);
        polygonButton.setOnClickListener(startPolygonListener);
        finReleve.setOnClickListener(stopListener);
        pointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Marker marker = Utils.createMarker(MainActivityRel.this,R.drawable.marker_green,getUsrLatLong());
                addLayer(marker);
            }
        });
    }

    private Releve createReleveToInsert(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        long creatorId = loginPreferences.getLong("usrId",0);
        return new Releve(creatorId,currentName,getCurrentReleveType(),Utils.getDate(),Utils.getTime());
    }

    /**
     * Vérifie s'il y a un relevé de terrain en cours
     *
     *
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean noReleveInProgress(){
        return currentReleve == NO_RELEVE;
    }

    protected boolean currentReleveIsLine() {
        return currentReleve == LINE;
    }

    protected boolean currentReleveIsPolygon() {
        return currentReleve == POLYGON;
    }

    protected String getCurrentReleveType(){
        if(currentReleveIsLine()){
            return getString(R.string.line);
        } else if(currentReleveIsPolygon()){
            return getString(R.string.polygon);
        } return getString(R.string.point);
    }

    /**
     * Instantie la polyLine
     */
    protected void instantiatePolyline(){
        polyline = new Polyline(Utils.createPaint(
                PAINT_STROKE,
                (int) (4 * myMap.getModel().displayModel.getScaleFactor()),
                Style.STROKE), AndroidGraphicFactory.INSTANCE);

        latLongsLine = polyline.getLatLongs();
    }

    /**
     * Créer un dialog d'avertissement pour éviter les fin de relevé par mégarde
     *
     * @return Un dialog d'avertissement
     */
    protected Dialog createAvertissementDialog(){
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.confirmMessage);
        builder.setTitle(getString(R.string.avertissement));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopReleve();
                setCurrentReleve(NO_RELEVE);
                finReleve.setVisibility(View.INVISIBLE);
            }
        });
        builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        box = builder.create();
        return box;
    }

    /**
     * Démarre le relevé de ligne
     */
    protected void startLine() {
        instantiatePolyline();
        polyline.addPoint(getUsrLatLong());
        addLayer(polyline);
        pointsTaker = new PointsTaker();
        pointsTaker.run();
    }

    /***
     * Stop le relevé en cours d'exécution
     */
    protected void stopReleve(){
        Releve rel = createReleveToInsert();
        dao.insertInventaire(rel);
        if(currentReleveIsLine())
            stopLine();
        else if(currentReleveIsPolygon())
            stopPolygon();
        nomReleve.setText("");
        nomReleveForm.setVisibility(View.VISIBLE);
    }

    /**
     * Stop le relevé de ligne
     */
    protected void stopLine() {
        handler.removeCallbacks(pointsTaker);
        lineLength = polylineLengthInMeters(latLongsLine);
        setLineLengthText();
    }

    protected void setLineLengthText(){
        lineLength_perimeterText.setText(getString(R.string.longueur) + lineLength);
    }

    protected double polylineLengthInMeters(List<LatLong> polyline){
        ListIterator<LatLong> it = polyline.listIterator();
        double res = 0;
        LatLong previous = null;
        LatLong current;
        try{
            previous = it.next();
        }catch (Exception e){
            e.printStackTrace();
        }
        while(it.hasNext()){
            current = it.next();
            res += previous.vincentyDistance(current);
            previous = current;
        }
        return res;
    }

    /**
     * Démarre un relevé de polygone
     */
    protected void startPolygon(){
        instantiatePolygon();
        polygon.addPoint(getUsrLatLong());
        addLayer(polygon);
        pointsTaker = new PointsTaker();
        pointsTaker.run();
    }

    protected void setPerimeterText(){
        lineLength_perimeterText.setText(getString(R.string.perimetre) + polygonPerimeter);
    }

    /**
     * Arrête le relevé de polygone
     */
    protected void stopPolygon() {
        handler.removeCallbacks(pointsTaker);
        addLayer(polygon);
        polygonPerimeter = getPolygonPerimeter(latLongsPolygon);
        setPerimeterText();
    }

    private double getPolygonPerimeter(List<LatLong> list){
        List<LatLong> tmp = new ArrayList<>(list);
        tmp.add(list.get(0));
        return polylineLengthInMeters(tmp);
    }

    /**
     * Instantie le polygonne
     */
    protected void instantiatePolygon() {
        polygon = new Polygon(paintFill, paintStroke, AndroidGraphicFactory.INSTANCE);;
        latLongsPolygon = polygon.getLatLongs();
    }

    /**
     * Action qui relève des points toutes les  secondes
     */
    protected class PointsTaker implements Runnable{

        /**
         * Délai entre chaque relevé de point en ms
         */
        protected static final int DELAY = 5000;

        @Override
        public void run() {
            addPointToGoodLayer();
            callGoodRedraw();
            handler.postDelayed(this,DELAY);
        }

        protected void addPointToGoodLayer(){
            if(currentReleveIsLine()){
                polyline.addPoint(getUsrLatLong());
            } else if(currentReleveIsPolygon()){
                polygon.addPoint(getUsrLatLong());
            }
        }

        protected void callGoodRedraw(){
            if(currentReleve == LINE){
                polyline.requestRedraw();
            } else if(currentReleve == POLYGON){
                polygon.requestRedraw();
            }
        }

    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main_rel);
    }

    @Override
    protected void setRelocButton(View.OnClickListener listener) {
        Button reloc2 = (Button) findViewById(R.id.reloc2);
        reloc2.setOnClickListener(listener);

    }

    protected void displayLayout(){
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.releveLayout);
        layout.setVisibility(View.VISIBLE);
    }

    /**
     * Ajoute le layer l à la carte
     *
     * @param l le layer à ajouter
     */
    protected void addLayer(Layer l){
        myMap.getLayers().add(l);
    }

    /**
     * Met l'identifiant du relevé courant à l'entier currentReleve en paramètre
     *
     * @param currentReleve L'identifiant du relevé en cours
     */
    public void setCurrentReleve(int currentReleve) {
        this.currentReleve = currentReleve;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}