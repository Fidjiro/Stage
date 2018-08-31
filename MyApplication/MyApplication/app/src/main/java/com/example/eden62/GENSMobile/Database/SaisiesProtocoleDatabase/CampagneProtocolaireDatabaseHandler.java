package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * BDD de campagne protocolaires
 */
public class CampagneProtocolaireDatabaseHandler extends SQLiteOpenHelper {

    public static final String RNF_TABLE_NAME = "RNF_campagnes";
    public static final String RNF_KEY = "_id";
    public static final String RNF_AUTHOR_ID = "author_id";
    public static final String RNF_NAME = "nom";
    public static final String RNF_DATE = "date";
    public static final String RNF_HEURE_DEBUT = "heure_debut";
    public static final String RNF_HEURE_FIN = "heure_fin";
    public static final String RNF_NOM_PROTO = "nom_proto";
    public static final String RNF_NOM_SITE = "nom_site";
    public static final String RNF_SAISIE = "saisie";
    public static final String RNF_TABLE_CREATE = "CREATE TABLE " + RNF_TABLE_NAME + " (" +
            RNF_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RNF_AUTHOR_ID + " INTEGER, " +
            RNF_NAME + " TEXT, " +
            RNF_DATE + " TEXT, " +
            RNF_HEURE_DEBUT + " TEXT, " +
            RNF_HEURE_FIN + " TEXT, " +
            RNF_NOM_PROTO + " TEXT, " +
            RNF_NOM_SITE + " TEXT, " +
            RNF_SAISIE + " TEXT);";
    public static final String RNF_TABLE_DROP = "DROP TABLE IF EXISTS " + RNF_TABLE_NAME + ";";

    public CampagneProtocolaireDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
