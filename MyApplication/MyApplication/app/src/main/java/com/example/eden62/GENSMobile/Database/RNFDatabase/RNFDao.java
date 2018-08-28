package com.example.eden62.GENSMobile.Database.RNFDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RNFDao {

    protected final int VERSION = 1;
    protected final static String NAME = "database4.db";

    protected SQLiteDatabase mDb;
    protected RNFDatabaseHandler mHandler;

    public static final String TABLE_NAME = "RNF_campagnes";
    public static final String KEY = "_id";
    public static final String NOM = "nom";
    public static final String DATE = "date";
    public static final String AUTHOR_ID = "author_id";
    public static final String TRANSECTS = "transects";
    public static final String METEO = "meteo";

    public RNFDao(Context pContext){
        this.mHandler = new RNFDatabaseHandler(pContext, NAME, null, VERSION);
    }

    public void open(){
        mDb = mHandler.getWritableDatabase();
    }

    public void close() {
        mDb.close();
    }

    public long insertRNFCampagne(RNFCampagne campagne){
        ContentValues cv = getCvFrom(campagne);
        return mDb.insert(TABLE_NAME, null, cv);
    }

    public long deleteRNFCampagne(RNFCampagne campagne){
        return mDb.delete(TABLE_NAME,KEY + " = ?", new String[]{campagne.get_id() + ""});
    }

    private ContentValues getCvFrom(RNFCampagne campagne){
        ContentValues cv = new ContentValues();
        cv.put(NOM,campagne.getName());
        cv.put(DATE,campagne.getDate());
        cv.put(AUTHOR_ID,campagne.getAuthor_id());
        cv.put(TRANSECTS,campagne.getTransects());
        cv.put(METEO,campagne.getMeteo());
        return cv;
    }

    private RNFCampagne dealWithSingleRowCursor(Cursor c){
        long _id = c.getLong(c.getColumnIndex(KEY));
        String nom  = c.getString(c.getColumnIndex(NOM));
        String date  = c.getString(c.getColumnIndex(DATE));
        long author_id = c.getLong(c.getColumnIndex(AUTHOR_ID));
        String transects = c.getString(c.getColumnIndex(METEO));
        String meteo = c.getString(c.getColumnIndex(METEO));

        return new RNFCampagne(_id, nom, date, author_id, transects, meteo);
    }

    protected List<RNFCampagne> dealWithCursor(Cursor c){
        List<RNFCampagne> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                RNFCampagne rel = dealWithSingleRowCursor(c);
                res.add(rel);
            } while(c.moveToNext());
        }

        c.close();
        return res;
    }
}
