package com.example.florian.myapplication.Database.CampagneDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Permets de créer la base de données campagne
 */
public class CampagneDatabaseHandler extends SQLiteOpenHelper {

    public static final String CAMPAGNE_TABLE_NAME = "Campagne";
    public static final String CAMPAGNE_KEY = "_id";
    public static final String CAMPAGNE_REF_TAXON = "ref_taxon";
    public static final String CAMPAGNE_REF_USR = "ref_usr";
    public static final String CAMPAGNE_NOM_FR = "nom_fr";
    public static final String CAMPAGNE_TYPE_TAXON = "type_taxon";
    public static final String CAMPAGNE_LATITUDE = "latitude";
    public static final String CAMPAGNE_LONGITUDE = "longitude";
    public static final String CAMPAGNE_DATE = "date";
    public static final String CAMPAGNE_NB = "nombre";
    public static final String CAMPAGNE_TYPE_OBS = "type_obs";
    public static final String CAMPAGNE_NBMALE = "nombre_mâle";
    public static final String CAMPAGNE_NBFEMALE = "nombre_femelle";
    public static final String CAMPAGNE_PRESENCE_PONTE = "presence_ponte";
    public static final String CAMPAGNE_ACTIVITE = "activite";
    public static final String CAMPAGNE_STATUT = "statut";
    public static final String CAMPAGNE_NIDIF = "nidification";
    public static final String CAMPAGNE_ABONDANCE = "indice_abondance";
    public static final String CAMPAGNE_TABLE_CREATE = "CREATE TABLE " + CAMPAGNE_TABLE_NAME + " (" +
            CAMPAGNE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPAGNE_REF_TAXON + " INTEGER NOT NULL, " +
            CAMPAGNE_REF_USR + " INTEGER NOT NULL, " +
            CAMPAGNE_NOM_FR + " STRING NOT NULL, " +
            CAMPAGNE_TYPE_TAXON + " INTEGER NOT NULL, " +
            CAMPAGNE_LATITUDE + " REAL NOT NULL, " +
            CAMPAGNE_LONGITUDE + " REAL NOT NULL, " +
            CAMPAGNE_DATE + " TEXT NOT NULL, " +
            CAMPAGNE_NB + " INTEGER, " +
            CAMPAGNE_TYPE_OBS + " TEXT NOT NULL, " +
            CAMPAGNE_NBMALE + " INTEGER, " +
            CAMPAGNE_NBFEMALE + " INTEGER, " +
            CAMPAGNE_PRESENCE_PONTE + " TEXT, " +
            CAMPAGNE_ACTIVITE + " TEXT, " +
            CAMPAGNE_STATUT + " TEXT, " +
            CAMPAGNE_NIDIF + " TEXT, " +
            CAMPAGNE_ABONDANCE + " INTEGER);";

    public static final String CAMPAGNE_TABLE_DROP = "DROP TABLE IF EXISTS " + CAMPAGNE_TABLE_NAME + ";";

    public CampagneDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CAMPAGNE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CAMPAGNE_TABLE_DROP);
        onCreate(db);
    }
}
