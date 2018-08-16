package com.example.eden62.GENSMobile.Database.RNFDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RNFDatabaseHandler extends SQLiteOpenHelper {

    public static final String RNF_TABLE_NAME = "RNF_campagnes";
    public static final String RNF_KEY = "_id";
    public static final String RNF_NAME = "nom";
    public static final String RNF_DATE = "date";
    public static final String RNF_AUTHOR_ID = "author_id";
    public static final String RNF_TRANSECTS = "transects";
    public static final String RNF_METEO = "meteo";
    public static final String RNF_TABLE_CREATE = "CREATE TABLE " + RNF_TABLE_NAME + " (" +
            RNF_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RNF_NAME + " TEXT, " +
            RNF_DATE + " TEXT, " +
            RNF_AUTHOR_ID + " INTEGER NOT NULL, " +
            RNF_TRANSECTS + " TEXT, " +
            RNF_METEO + " TEXT);";
    public static final String RNF_TABLE_DROP = "DROP TABLE IF EXISTS " + RNF_TABLE_NAME + ";";

    public RNFDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RNF_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RNF_TABLE_DROP);
        onCreate(db);
    }
}
