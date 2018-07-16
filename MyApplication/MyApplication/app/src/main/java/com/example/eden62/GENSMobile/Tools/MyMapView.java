package com.example.eden62.GENSMobile.Tools;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.DragEvent;
import android.view.View;

import com.example.eden62.GENSMobile.R;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polygon;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.view.InputListener;

import java.io.File;
import java.io.IOException;

/**
 * Représente l'objet mapView des MapActivity
 */
public class MyMapView {

    private Byte baseZoom = 19;
    private Marker positionMarker;
    private Byte minZoom = 9;
    private Byte maxZoom = 21;
    private String mapFileName = "nord-pas-de-calais.map";
    private MapView mapView;

    public static final int SHAPE_LAYER_INDEX = 1;
    public static final int POSITION_LAYER_INDEX = 2;

    private TileCache myCache;


    public MyMapView(MapView mapView) {
        this.mapView = mapView;
        initMap();

    }

    // Permet de récupérer le fichier qui contient la carte hors-ligne

    /**
     * Initialise la carte
     */
    private void initMap() {
        //On la charge
        setMapFile();

        //Réglage du zoom
        setZoom();

        //Ajout d'un marqueur
        addMarker();
    }

    /**
     * Set le zoom et ses propriétés
     */
    private void setZoom(){
        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setZoomLevelMax(maxZoom);
        mapView.setZoomLevelMin(minZoom);
        mapView.setZoomLevel(baseZoom);
    }

    /**
     * Permet d'ajouter un OnDragListener à la map
     *
     * @param listener Le listener à ajouter
     */
    public void addOnInputListener(InputListener listener){
        mapView.addInputListener(listener);
    }

    /**
     * Ajoute un marqueur
     */
    private void addMarker(){
        positionMarker = Utils.createMarker(mapView.getContext(),R.drawable.marker_red,null);
        System.out.println(getLayers().get(0));
        getLayers().add(new Polygon(null,null,null));
        mapView.getLayerManager().getLayers().add(positionMarker);
    }

    public int getMarkerIdx(){
        return getLayers().indexOf(positionMarker);
    }

    /**
     * Récupère et set le fond de carte
     */
    private void setMapFile(){
        File file = getMapSourceFileFromAssets(mapFileName);
        if(file != null) {
            if(externalStorageAvailable())
                myCache = generateTileCache();
            MapDataStore mapDataStore = new MapFile(file);
            TileRendererLayer tileRendererLayer = generateTileRenderer(myCache,mapDataStore);
            mapView.getLayerManager().getLayers().add(tileRendererLayer);
        }
    }

    public void redrawMarker(){
        positionMarker.requestRedraw();
    }

    /**
     * Vérifie s'il il reste de la place dans le stockage du téléphone
     *
     * @return <code>True</code> si il y a de la place, <code>false</code> sinon
     */
    private boolean externalStorageAvailable() {
        return
                Environment.MEDIA_MOUNTED
                        .equals(Environment.getExternalStorageState());
    }

    /**
     * Récupère le fond de carte nommé par fileName
     *
     * @param fileName Le nom du fond de carte
     * @return Le fichier de nom fileName
     */
    private File getMapSourceFileFromAssets(String fileName){
        File file = null;
        try {
            file = Utils.getFileFromAssets(mapView.getContext(),fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Génère un tileCache pour ne pas charger tout le temps la carte
     *
     * @return Un nouveau tileCache
     */
    @NonNull
    private TileCache generateTileCache(){
        return AndroidUtil.createTileCache(mapView.getContext(), "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());
    }

    /**
     * A map can have several layers,
     * stacked on top of each other. A layer can be a map or some visual elements, such as
     * markers. Here we only show a map based on a mapsforge map file. For this we need a
     * TileRendererLayer. A TileRendererLayer needs a TileCache to hold the generated map
     * tiles, a map file from which the tiles are generated and Rendertheme that defines the
     * appearance of the map. (mapsforges doc)
     */
    private TileRendererLayer generateTileRenderer(TileCache tileCache, MapDataStore mapDataStore){
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);

        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

        return tileRendererLayer;
    }

    /**
     * Méthode utilisé lors du onDestroy(), qui clear la map et tileCache
     */
    public void exit(){
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
    }

    /**
     * Centre la carte sur la position lat
     *
     * @param lat La position sur laquelle la map doit être centrée
     */
    public void centerOn(LatLong lat){
        //mapView.setZoomLevel(baseZoom);
        mapView.getModel().mapViewPosition.animateTo(lat);
    }

    /**
     * Mets le marqueur à la localisation loc
     *
     * @param loc La localisation où mettre le marqueur
     */
    public void setMyMarkerOn(LatLong loc) {
        positionMarker.setLatLong(loc);
    }

    /**
     * @see MapView#getModel()
     * @return Le model de la carte
     */
    public Model getModel(){
        return mapView.getModel();
    }

    /**
     * Récupère les layers de cette map
     *
     * @return les layers de cette map
     */
    public Layers getLayers(){
        return mapView.getLayerManager().getLayers();
    }
}
