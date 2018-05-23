package com.example.florian.myapplication.Tools;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.util.ArrayList;
import java.util.List;

public class MyPolygon {

    protected org.mapsforge.map.layer.overlay.Polygon polygonMapsforge;
    protected org.locationtech.jts.geom.Polygon polygonJTS;
    protected List<Coordinate> coordinates;
    protected Paint paintFill = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.GREEN), 2,
            Style.FILL);
    protected Paint paintStroke = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.BLACK), 2,
            Style.STROKE);

    public MyPolygon(){
        instantiatePolygon();
        coordinates = new ArrayList<>();
    }

    /**
     * Instantie le polygonne
     */
    protected void instantiatePolygon() {
        polygonMapsforge = new org.mapsforge.map.layer.overlay.Polygon(paintFill, paintStroke, AndroidGraphicFactory.INSTANCE);
    }

    public List<LatLong> getLatLongs(){
        return polygonMapsforge.getLatLongs();
    }

    public void addPoint(LatLong latLong){
        Coordinate c = new Coordinate(latLong.longitude,latLong.latitude);
        polygonMapsforge.addPoint(latLong);
        coordinates.add(c);
    }

    public org.mapsforge.map.layer.overlay.Polygon getLayer(){
        return this.polygonMapsforge;
    }

    public void requestRedraw(){
        polygonMapsforge.requestRedraw();
    }

    public double getArea(){
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing((Coordinate[])coordinates.toArray());
        polygonJTS = geometryFactory.createPolygon(linearRing, null);
        return polygonJTS.getArea();
    }
}
