package com.example.eden62.GENSMobile.Database.RNFDatabase;

import android.os.Parcel;
import android.os.Parcelable;

public class Transect implements Parcelable{

    private String name;
    private int length;
    private RNFInventories inventories;
    private String time;
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
        //parcel.writeString(nebulosite);
        parcel.writeString(time);
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
        //nebulosite = in.readString();
        time = in.readString();
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
