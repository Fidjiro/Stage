package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eden62.GENSMobile.Database.DatabaseItem;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFInventaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.Transect;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CampagneProtocolaire<T extends Saisie> implements DatabaseItem, Parcelable{

    protected long _id;
    protected long author_id;
    protected String name;
    protected String date;
    protected String heureDebut;
    protected String heureFin;
    protected String nomProto;
    protected String nomSite;
    // La saisie est transformée en json pour pouvoir être stocké dans la base de données
    protected String saisie;

    public CampagneProtocolaire(long author_id, String name, String date, String heureDebut, String heureFin, String nomProto, String nomSite, String saisie) {
        this.author_id = author_id;
        this.name = name;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.nomSite = nomSite;
        this.nomProto = nomProto;
        this.saisie = saisie;
    }

    public CampagneProtocolaire(long _id, long author_id, String name, String date, String heureDebut, String heureFin, String nomProto, String nomSite, String saisie) {
        this(author_id, name, date, heureDebut, heureFin, nomProto, nomSite, saisie);
        this._id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeLong(author_id);
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeString(heureDebut);
        parcel.writeString(heureFin);
        parcel.writeString(nomProto);
        parcel.writeString(nomSite);
        parcel.writeString(saisie);
    }

    public static final Parcelable.Creator<CampagneProtocolaire> CREATOR = new Parcelable.Creator<CampagneProtocolaire>() {
        public CampagneProtocolaire createFromParcel(Parcel in) {
            return new CampagneProtocolaire(in);
        }

        public CampagneProtocolaire[] newArray(int size) {
            return new CampagneProtocolaire[size];
        }
    };

    private CampagneProtocolaire(Parcel in) {
        _id = in.readLong();
        author_id = in.readLong();
        name = in.readString();
        date = in.readString();
        heureDebut = in.readString();
        heureFin = in.readString();
        nomProto = in.readString();
        nomSite = in.readString();
        saisie = in.readString();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public String getNomProto() {
        return nomProto;
    }

    public void setNomProto(String nomProto) {
        this.nomProto = nomProto;
    }

    public String getSaisie() {
        return saisie;
    }

    public void setSaisie(String saisie) {
        this.saisie = saisie;
    }

    public T getSaisieFromJson(Class<T> classT){
        Gson gson = new Gson();
        T res = gson.fromJson(getSaisie(),classT);
        return res;
    }
}
