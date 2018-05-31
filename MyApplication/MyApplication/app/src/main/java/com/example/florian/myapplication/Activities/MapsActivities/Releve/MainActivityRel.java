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
import android.widget.TextView;

import com.example.florian.myapplication.Activities.MapsActivities.MainActivity;
import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.overlay.Polygon;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Math.log;

/**
 * Activité carte pour les relevés de terrain
 */
public class MainActivityRel extends MainActivity {

    protected HistoryDao dao;

    protected ImageButton lineButton, polygonButton, pointButton;
    protected EditText nomReleve;
    protected TextView lineLengthText,perimeterText,positionWGSText,positionL93Text;
    protected LinearLayout nomReleveForm;
    protected Button validNom, finReleve, mesReleve;

    protected Releve releveToAdd;

    protected Handler handler;
    protected Runnable pointsTaker;

    protected Polyline polyline;
    protected double lineLength;
    protected double polygonPerimeter;
    protected Polygon polygon;
    protected List<LatLong> latLongs;
    protected LatLong lastMarkerPosition;

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

    private static final int BOITE_FIN_RELEVE = 3;
    private static final int BOITE_FIN_NOMMAGE_RELEVE = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        dao = new HistoryDao(this);
        dao.open();

        tryGetArea();

        lineButton = (ImageButton) findViewById(R.id.bouton_releve_ligne);
        polygonButton = (ImageButton) findViewById(R.id.bouton_releve_polygone);
        pointButton = (ImageButton) findViewById(R.id.bouton_releve_point);
        nomReleve = (EditText) findViewById(R.id.nomRel);
        nomReleveForm = (LinearLayout) findViewById(R.id.nomReleveLayout);
        validNom = (Button) findViewById(R.id.validerNom);
        finReleve = (Button) findViewById(R.id.finReleve);
        mesReleve = (Button) findViewById(R.id.mesReleves);
        lineLengthText = (TextView) findViewById(R.id.lineLength);
        perimeterText = (TextView) findViewById(R.id.perimeter);
        positionWGSText = (TextView) findViewById(R.id.positionWGS);
        positionL93Text = (TextView) findViewById(R.id.positionL93);

        mesReleve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRel.this,HistoryReleveActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener stopListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAvertissementDialog(BOITE_FIN_RELEVE).show();
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
                if(!nomReleve.getText().toString().isEmpty()) {
                    releveToAdd.setNom(nomReleve.getText().toString());
                    dao.insertInventaire(releveToAdd);
                    nomReleveForm.setVisibility(View.INVISIBLE);
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nomReleve.getWindowToken(), 0);
                }else
                    createAvertissementDialog(BOITE_FIN_NOMMAGE_RELEVE).show();
            }
        });

        lineButton.setOnClickListener(startLineListener);
        polygonButton.setOnClickListener(startPolygonListener);
        finReleve.setOnClickListener(stopListener);
        pointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Marker marker = Utils.createMarker(MainActivityRel.this,R.drawable.marker_green,getUsrLatLong());
                lastMarkerPosition = marker.getLatLong();
                addLayer(marker);
                finirReleve();
            }
        });
    }

    private void tryGetArea(){
        List<LatLong> latLongs = new ArrayList<>();
        latLongs.add(new LatLong(-5.9838563,-1.3630812));
        latLongs.add(new LatLong(-5.9838501,-1.3630816));
        latLongs.add(new LatLong(-5.9838498,-1.3630753));
        latLongs.add(new LatLong(-5.9838560,-1.3630750));
        latLongs.add(new LatLong(-5.9838563,-1.3630812));
        System.out.println("area :" + getArea(latLongs));
    }

    private double atanh(double x){
        return (log(1+x) - log(1-x))/2;
    }

    private XY convertWgs84ToL93(LatLong latLong){

        double latitude = latLong.getLatitude();
        double longitude = latLong.getLongitude();

// définition des constantes
        double c= 11754255.426096; //constante de la projection
        double e= 0.0818191910428158; //première exentricité de l'ellipsoïde
        double n= 0.725607765053267; //exposant de la projection
        double xs= 700000; //coordonnées en projection du pole
        double ys= 12655612.049876; //coordonnées en projection du pole

// pré-calculs
        double lat_rad= latitude/180*Math.PI; //latitude en rad
        double lat_iso= atanh(Math.sin(lat_rad))-e*atanh(e*Math.sin(lat_rad)); //latitude isométrique

//calcul
        double x= ((c*Math.exp(-n*(lat_iso)))*Math.sin(n*(longitude-3)/180*Math.PI)+xs);
        double y= (ys-(c*Math.exp(-n*(lat_iso)))*Math.cos(n*(longitude-3)/180*Math.PI));
        return new XY(x,y);
    }

    protected List<XY> convertLatLongsListToL93List(List<LatLong> latLongs){
        List<XY> coordList = new ArrayList<>();

        for(LatLong latLong : latLongs){
            coordList.add(convertWgs84ToL93(latLong));
        }

        return coordList;
    }

    //Fonctionne mais attention, le point d'origine doit également se retrouver à la fin de la liste
    protected double getArea(List<LatLong> latLongs){
        // transformer chaque latLong en L93
        List<XY> listFormatedCoord = convertLatLongsListToL93List(latLongs);

        int nbCoord = listFormatedCoord.size();
        Iterator<XY> it = listFormatedCoord.iterator();

        if(nbCoord < 3) {
            return 0;
        }

        XY pp = new XY();
        XY cp = it.next();
        XY np = it.next();
        double x0 = cp.x;
        np.x -= x0;
        double sum = 0;

        int i = 1;
        while(it.hasNext()){
            pp.y = cp.y;
            cp.x = np.x;
            cp.y = np.y;
            np = it.next();
            np.x -= x0;
            sum += cp.x * (np.y - pp.y);
        }
        return Math.abs(-sum /2);
    }

    private class XY{
        public double x;
        public double y;

        public XY(){
            x = 0;
            y = 0;
        }

        public XY(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private Releve createReleveToInsert(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        long creatorId = loginPreferences.getLong("usrId",0);
        String latitude;
        String longitude;
        String formatedLatLongs = "";
        if(currentReleveIsPolygon() ||currentReleveIsLine()){
            StringTuple tuple = getStringsFromLatLongs();
            latitude = tuple.fst();
            longitude = tuple.snd();
            formatedLatLongs = getFormatedLatLongs();
        } else{
            latitude = lastMarkerPosition.getLatitude() + "";
            longitude = lastMarkerPosition.getLongitude() + "";
        }
        return new Releve(creatorId,"",getCurrentReleveType(), latitude, longitude, formatedLatLongs, "false", Utils.getDate(),Utils.getTime());
    }

    private StringTuple getStringsFromLatLongs(){
        String lats = "";
        String longs = "";
        for(LatLong latLong : latLongs){
            double lat = latLong.getLatitude();
            double lon = latLong.getLongitude();
            lats += lat + ";";
            longs += lon + ";";
        }
        return new StringTuple(lats,longs);
    }

    private String getFormatedLatLongs(){
        Gson gson = new Gson();
        return gson.toJson(latLongs);
    }

    private class StringTuple{

        private String fst;
        private String snd;

        public StringTuple(String fst, String snd) {
            this.fst = fst;
            this.snd = snd;
        }

        public String fst(){
            return fst;
        }

        public String snd(){
            return snd;
        }


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

    public Polyline createPolyline(){
        return new Polyline(Utils.createPaint(
                PAINT_STROKE,
                (int) (4 * myMap.getModel().displayModel.getScaleFactor()),
                Style.STROKE), AndroidGraphicFactory.INSTANCE);
    }

    /**
     * Instantie la polyLine
     */
    protected void instantiatePolyline(){
        polyline = createPolyline();

        latLongs = polyline.getLatLongs();
    }

    private void finirReleve(){
        stopReleve();
        setCurrentReleve(NO_RELEVE);
        finReleve.setVisibility(View.INVISIBLE);
    }

    /**
     * Créer un dialog d'avertissement pour éviter les fin de relevé par mégarde
     *
     * @return Un dialog d'avertissement
     */
    protected Dialog createAvertissementDialog(int identifiant){
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (identifiant) {
            case BOITE_FIN_RELEVE:
                builder.setMessage(R.string.confirmMessage);
                builder.setTitle(getString(R.string.avertissement));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finirReleve();
                    }
                });
                builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                break;
            case BOITE_FIN_NOMMAGE_RELEVE:
                builder.setMessage(getString(R.string.nommezReleve));
                builder.setTitle(getString(R.string.avertissement));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        }
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

    private void makePositionsTextVisible(){
        lineLengthText.setVisibility(View.GONE);
        perimeterText.setVisibility(View.GONE);
        positionL93Text.setVisibility(View.VISIBLE);
        positionWGSText.setVisibility(View.VISIBLE);
    }

    protected void stopPoint(){
        makePositionsTextVisible();
        setPositionText();
    }

    protected void setPositionText(){
        double lat = lastMarkerPosition.getLatitude();
        double lon = lastMarkerPosition.getLongitude();

        positionWGSText.setText(getString(R.string.positionWGS) + " " + lat + " ; " + lon);
        positionL93Text.setText(getString(R.string.positionL93));
    }

    /***
     * Stop le relevé en cours d'exécution
     */
    protected void stopReleve(){
        releveToAdd = createReleveToInsert();
        if(currentReleveIsLine())
            stopLine();
        else if(currentReleveIsPolygon())
            stopPolygon();
        else{
            stopPoint();
        }
        nomReleve.setText("");
        nomReleveForm.setVisibility(View.VISIBLE);
    }

    private void makeLineLengthTextVisible(){
        lineLengthText.setVisibility(View.VISIBLE);
        perimeterText.setVisibility(View.GONE);
        positionL93Text.setVisibility(View.GONE);
        positionWGSText.setVisibility(View.GONE);
    }

    /**
     * Stop le relevé de ligne
     */
    protected void stopLine() {
        handler.removeCallbacks(pointsTaker);
        lineLength = polylineLengthInMeters(latLongs);

        makeLineLengthTextVisible();
        setLineLengthText();
    }

    protected void setLineLengthText(){
        lineLengthText.setText(getString(R.string.longueur) + lineLength);
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

    private void makePerimeterTextVisible(){
        lineLengthText.setVisibility(View.GONE);
        perimeterText.setVisibility(View.VISIBLE);
        positionL93Text.setVisibility(View.GONE);
        positionWGSText.setVisibility(View.GONE);
    }

    protected void setPerimeterText(){
        perimeterText.setText(getString(R.string.perimetre) + polygonPerimeter);
    }

    /**
     * Arrête le relevé de polygone
     */
    protected void stopPolygon() {
        handler.removeCallbacks(pointsTaker);
        polygonPerimeter = getPolygonPerimeter(latLongs);
        makePerimeterTextVisible();
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
        polygon = new Polygon(paintFill, paintStroke, AndroidGraphicFactory.INSTANCE);
        latLongs = polygon.getLatLongs();
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

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Releve rel = intent.getParcelableExtra("releve");
        if (rel != null) {
            redrawReleve(rel);
        }
    }

    private void redrawReleve(Releve rel){
        Gson gson = new Gson();
        String relType = rel.getType();
        if (relType.equals("Point")) {
            redrawPointReleve(rel);
        } else {
            Type type = new TypeToken<List<LatLong>>() {
            }.getType();
            String relLatLongsString = rel.getLat_long();
            List<LatLong> relLatLongs = gson.fromJson(relLatLongsString, type);
            if (relType.equals("Ligne")) {
                Polyline polylineRel = createPolyline();
                polylineRel.getLatLongs().addAll(relLatLongs);
                addLayer(polylineRel);
            } else {
                Polygon relPolygon = new Polygon(paintFill, paintStroke, AndroidGraphicFactory.INSTANCE);
                relPolygon.getLatLongs().addAll(relLatLongs);
                addLayer(relPolygon);
            }
        }
    }

    private void redrawPointReleve(Releve rel){
        LatLong latLong = new LatLong(Double.parseDouble(rel.getLatitudes()), Double.parseDouble(rel.getLongitudes()));
        Marker m = Utils.createMarker(MainActivityRel.this, R.drawable.marker_green, latLong);
        addLayer(m);
    }
}