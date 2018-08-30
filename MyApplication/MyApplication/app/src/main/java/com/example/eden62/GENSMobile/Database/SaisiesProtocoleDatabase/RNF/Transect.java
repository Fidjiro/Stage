package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF;

import android.os.Parcel;
import android.os.Parcelable;

public class Transect implements Parcelable{

    private String name;
    private int length;
    private RNFInventories inventories = new RNFInventories();
    private int minutes;
    private int secondes;
    private String info = "";
    private boolean done;

    public Transect(String name, int length) {
        this.name = name;
        this.length = length;
        this.done = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(length);
        parcel.writeTypedList(inventories);
        parcel.writeInt(minutes);
        parcel.writeInt(secondes);
        parcel.writeString(info);
        parcel.writeByte((byte) (done ? 1:0));
    }

    public static final Parcelable.Creator<Transect> CREATOR = new Parcelable.Creator<Transect>() {
        public Transect createFromParcel(Parcel in) {
            return new Transect(in);
        }

        public Transect[] newArray(int size) {
            return new Transect[size];
        }
    };

    private Transect(Parcel in) {
        name = in.readString();
        length = in.readInt();
        in.readTypedList(inventories,RNFInventaire.CREATOR);
        minutes = in.readInt();
        secondes = in.readInt();
        info = in.readString();
        done = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSecondes() {
        return secondes;
    }

    public void setSecondes(int secondes) {
        this.secondes = secondes;
    }

    private String getTimeString(){
        if(getMinutes() == 0 && getSecondes() == 0 )
            return "";
        return " [durée: " + minutes + "m" + secondes + "s]";
    }

    public RNFInventories getInventories() {
        return inventories;
    }

    public void setInventories(RNFInventories inventories) {
        this.inventories = inventories;
    }

    public String getInfo() {
        return info.equals("") ? info : " - " + info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return getName() + " (" + getLength() + "m)" + getInfo() + getTimeString();
    }
}
