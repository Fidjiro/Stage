package com.example.eden62.GENSMobile.Database.ReleveDatabase;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eden62.GENSMobile.Database.DatabaseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mapsforge.core.model.LatLong;

import java.lang.reflect.Type;
import java.util.List;

public class Releve implements Parcelable,DatabaseItem{

    private long _id;
    private long creator;
    private String nom;
    private String type;
    private String latitudes;
    private String longitudes;
    private String lat_long;
    private String importStatus;
    private String date;
    private String heure;
    private double length;
    private double perimeter;
    private double area;

    public Releve(long creator, String nom, String type, String latitudes, String longitudes, String lat_long, String importStatus, String date, String heure, double length, double perimeter, double area) {
        this.creator = creator;
        this.nom = nom;
        this.type = type;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        this.lat_long = lat_long;
        this.importStatus = importStatus;
        this.date = date;
        this.heure = heure;
        this.length = length;
        this.perimeter = perimeter;
        this.area = area;
    }

    public Releve(long _id, long creator, String nom, String type, String latitudes, String longitudes, String lat_long, String importStatus, String date, String heure, double length, double perimeter, double area) {
        this(creator, nom, type, latitudes, longitudes, lat_long, importStatus, date, heure, length, perimeter, area);
        this._id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeLong(creator);
        parcel.writeString(nom);
        parcel.writeString(type);
        parcel.writeString(latitudes);
        parcel.writeString(longitudes);
        parcel.writeString(lat_long);
        parcel.writeString(importStatus);
        parcel.writeString(date);
        parcel.writeString(heure);
        parcel.writeDouble(length);
        parcel.writeDouble(perimeter);
        parcel.writeDouble(area);
    }

    public static final Parcelable.Creator<Releve> CREATOR = new Parcelable.Creator<Releve>() {
        public Releve createFromParcel(Parcel in) {
            return new Releve(in);
        }

        public Releve[] newArray(int size) {
            return new Releve[size];
        }
    };

    private Releve(Parcel in) {
        _id = in.readLong();
        creator = in.readLong();
        nom = in.readString();
        type = in.readString();
        latitudes = in.readString();
        longitudes = in.readString();
        lat_long = in.readString();
        importStatus = in.readString();
        date = in.readString();
        heure = in.readString();
        length = in.readDouble();
        perimeter = in.readDouble();
        area = in.readDouble();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(String latitudes) {
        this.latitudes = latitudes;
    }

    public String getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(String longitudes) {
        this.longitudes = longitudes;
    }

    public String getLat_long() {
        return lat_long;
    }

    public List<LatLong> getLat_longFromJson(){
        Gson gson = new Gson();
        Type type = new TypeToken<List<LatLong>>() {}.getType();
        return gson.fromJson(getLat_long(), type);
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        return (int)this._id;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Releve){
            Releve other = (Releve) o;
            return this.hashCode() == other.hashCode() && this.heure.equals(other.heure);
        }return false;
    }
}
