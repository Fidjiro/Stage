package com.example.florian.myapplication.Database.ReleveDatabase;

public class Releve {
    private long _id;
    private long creator;
    private String nom;
    private String type;
    private String date;
    private String heure;

    public Releve(long creator, String nom, String type, String date, String heure) {
        this.creator = creator;
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.heure = heure;
    }

    public Releve(long _id, long creator, String nom, String type, String date, String heure) {
        this(creator,nom,type,date,heure);
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
