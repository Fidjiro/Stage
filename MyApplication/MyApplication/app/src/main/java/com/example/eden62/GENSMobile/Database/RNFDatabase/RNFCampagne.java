package com.example.eden62.GENSMobile.Database.RNFDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RNFCampagne {

    private long _id;
    protected String name;
    protected String date;
    protected long author_id;
    protected String transects;
    protected String meteo;

    public RNFCampagne(String name, String date, long author_id, String transects, String meteo) {
        this.name = name;
        this.date = date;
        this.author_id = author_id;
        this.transects = transects;
        this.meteo = meteo;
    }

    public RNFCampagne(long _id, String name, String date, long author_id, String transects, String meteo) {
        this(name, date, author_id, transects, meteo);
        this._id = _id;
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

    public String getMeteo() {
        return meteo;
    }

    public RNFMeteo getMeteoFromJson(){
        Gson gson = new Gson();
        Type type = new TypeToken<RNFMeteo>() {}.getType();
        return gson.fromJson(getMeteo(), type);
    }

    public List<Transect> getTransectsFromJson(){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Transect>>() {}.getType();
        return gson.fromJson(getTransects(),type);
    }

    public void setMeteo(String meteo) {
        this.meteo = meteo;
    }

    public String getTransects() {
        return transects;
    }

    public void setTransects(String transects) {
        this.transects = transects;
    }
}
