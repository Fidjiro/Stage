package com.example.eden62.GENSMobile.Database.RNFDatabase;

import android.os.Parcel;
import android.os.Parcelable;

public class RNFInventaire implements Parcelable{

    protected String nomLatin;
    protected String nomFr;
    protected int nombre;
    protected int nbMale;
    protected int nbFemale;

    public RNFInventaire(String nomLatin, String nomFr, int nombre, int nbMale, int nbFemale) {
        this.nomLatin = nomLatin;
        this.nomFr = nomFr;
        this.nombre = nombre;
        this.nbMale = nbMale;
        this.nbFemale = nbFemale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nomLatin);
        parcel.writeString(nomFr);
        parcel.writeInt(nombre);
        parcel.writeInt(nbMale);
        parcel.writeInt(nbFemale);
    }

    public static final Parcelable.Creator<RNFInventaire> CREATOR = new Parcelable.Creator<RNFInventaire>() {
        public RNFInventaire createFromParcel(Parcel in) {
            return new RNFInventaire(in);
        }

        public RNFInventaire[] newArray(int size) {
            return new RNFInventaire[size];
        }
    };

    private RNFInventaire(Parcel in) {
        nomLatin = in.readString();
        nomFr = in.readString();
        nombre = in.readInt();
        nbMale = in.readInt();
        nbFemale = in.readInt();
    }

    public String getNomLatin() {
        return nomLatin;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public String getNomFr() {
        return nomFr;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public int getNbMale() {
        return nbMale;
    }

    public void setNbMale(int nbMale) {
        this.nbMale = nbMale;
    }

    public int getNbFemale() {
        return nbFemale;
    }

    public void setNbFemale(int nbFemale) {
        this.nbFemale = nbFemale;
    }
}
