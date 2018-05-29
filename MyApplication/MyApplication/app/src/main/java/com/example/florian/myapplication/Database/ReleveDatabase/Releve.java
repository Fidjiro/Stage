package com.example.florian.myapplication.Database.ReleveDatabase;

public class Releve {

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

    public Releve(long creator, String nom, String type, String latitudes, String longitudes, String lat_long, String importStatus, String date, String heure) {
        this.creator = creator;
        this.nom = nom;
        this.type = type;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        this.lat_long = lat_long;
        this.importStatus = importStatus;
        this.date = date;
        this.heure = heure;
    }

    public Releve(long _id, long creator, String nom, String type, String latitudes, String longitudes, String lat_long, String importStatus, String date, String heure) {
        this(creator,nom,type, latitudes, longitudes, lat_long, importStatus,date,heure);
        this._id = _id;
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
}
