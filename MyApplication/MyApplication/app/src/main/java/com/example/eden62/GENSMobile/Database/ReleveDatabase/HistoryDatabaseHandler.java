package com.example.eden62.GENSMobile.Database.ReleveDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDatabaseHandler extends SQLiteOpenHelper {

    public static final String HISTORY_TABLE_NAME = "History";
    public static final String HISTORY_KEY = "_id";
    public static final String HISTORY_CREATOR = "creator";
    public static final String HISTORY_NOM = "nom";
    public static final String HISTORY_TYPE = "type";
    public static final String HISTORY_LATITUDES = "latitudes";
    public static final String HISTORY_LONGITUDES = "longitudes";
    public static final String HISTORY_LAT_LONG = "lat_longs";
    public static final String HISTORY_IMPORT = "import";
    public static final String HISTORY_DATE = "date";
    public static final String HISTORY_TIME = "heure";
    public static final String HISTORY_LENGTH = "longueur";
    public static final String HISTORY_PERIMETER = "perimetre";
    public static final String HISTORY_AREA = "surface";
    public static final String HISTORY_TABLE_CREATE = "CREATE TABLE " + HISTORY_TABLE_NAME + " (" +
            HISTORY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HISTORY_CREATOR + " INTEGER NOT NULL, " +
            HISTORY_NOM + " TEXT, " +
            HISTORY_TYPE + " TEXT NOT NULL, " +
            HISTORY_LATITUDES + " TEXT NOT NULL, " +
            HISTORY_LONGITUDES + " TEXT NOT NULL, " +
            HISTORY_LAT_LONG + " TEXT NOT NULL, " +
            HISTORY_IMPORT + " TEXT NOT NULL, " +
            HISTORY_DATE + " TEXT NOT NULL, " +
            HISTORY_TIME + " TEXT, " +
            HISTORY_LENGTH + " REAL, " +
            HISTORY_PERIMETER + " REAL, " +
            HISTORY_AREA + " REAL);";
    public static final String HISTORY_TABLE_DROP = "DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME + ";";

    public HistoryDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HISTORY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(HISTORY_TABLE_DROP);
        onCreate(db);
    }
}
