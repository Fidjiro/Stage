package com.example.eden62.GENSMobile.Database.RNFDatabase;

import java.util.List;

public class RNFCampagne {

    private long _id;
    protected String name;
    protected String date;
    protected long author_id;
    protected List<Transect> transects;
    protected RNFMeteo meteo;

    public RNFCampagne(long _id, String name, String date, long author_id, RNFMeteo meteo, List<Transect> transects) {
        this._id = _id;
        this.name = name;
        this.date = date;
        this.author_id = author_id;
        this.meteo = meteo;
        this.transects = transects;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public RNFMeteo getMeteo() {
        return meteo;
    }

    public void setMeteo(RNFMeteo meteo) {
        this.meteo = meteo;
    }

    public List<Transect> getTransects() {
        return transects;
    }

    public void setTransects(List<Transect> transects) {
        this.transects = transects;
    }
}
