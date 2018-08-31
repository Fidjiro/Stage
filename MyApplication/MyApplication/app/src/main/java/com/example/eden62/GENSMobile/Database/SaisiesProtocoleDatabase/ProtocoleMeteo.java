package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Objet représentant une météo pour les campagnes protocolaires
 */
public class ProtocoleMeteo implements Parcelable{

    protected String visibilite;
    protected String precipitation;
    protected String nebulosite;
    protected float temperatureAir;
    protected String directionVent;
    protected String vitesseVent;

    public ProtocoleMeteo(String visibilite, String precipitation, String nebulosite, float temperatureAir, String directionVent, String vitesseVent) {
        this.visibilite = visibilite;
        this.precipitation = precipitation;
        this.nebulosite = nebulosite;
        this.temperatureAir = temperatureAir;
        this.directionVent = directionVent;
        this.vitesseVent = vitesseVent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(visibilite);
        parcel.writeString(precipitation);
        parcel.writeString(nebulosite);
        parcel.writeFloat(temperatureAir);
        parcel.writeString(directionVent);
        parcel.writeString(vitesseVent);
    }

    public static final Parcelable.Creator<ProtocoleMeteo> CREATOR = new Parcelable.Creator<ProtocoleMeteo>() {
        public ProtocoleMeteo createFromParcel(Parcel in) {
            return new ProtocoleMeteo(in);
        }

        public ProtocoleMeteo[] newArray(int size) {
            return new ProtocoleMeteo[size];
        }
    };

    private ProtocoleMeteo(Parcel in) {
        visibilite = in.readString();
        precipitation = in.readString();
        nebulosite = in.readString();
        temperatureAir = in.readFloat();
        directionVent = in.readString();
        vitesseVent = in.readString();
    }

    public String getVisibilite() {
        return visibilite;
    }

    public void setVisibilite(String visibilite) {
        this.visibilite = visibilite;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getNebulosite() {
        return nebulosite;
    }

    public void setNebulosite(String nebulosite) {
        this.nebulosite = nebulosite;
    }

    public float getTemperatureAir() {
        return temperatureAir;
    }

    public void setTemperatureAir(float temperatureAir) {
        this.temperatureAir = temperatureAir;
    }

    public String getDirectionVent() {
        return directionVent;
    }

    public void setDirectionVent(String directionVent) {
        this.directionVent = directionVent;
    }

    public String getVitesseVent() {
        return vitesseVent;
    }

    public void setVitesseVent(String vitesseVent) {
        this.vitesseVent = vitesseVent;
    }
}
