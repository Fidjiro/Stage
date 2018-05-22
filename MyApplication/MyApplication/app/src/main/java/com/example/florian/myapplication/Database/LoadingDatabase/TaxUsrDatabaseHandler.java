package com.example.florian.myapplication.Database.LoadingDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Permet de créer la base de données qui contient les tables Taxref et User
 */
public class TaxUsrDatabaseHandler extends SQLiteOpenHelper {

    public static final String TAXREF_TABLE_NAME = "Taxref";
    public static final String TAXREF_KEY = "_id";
    public static final String TAXREF_NOM = "nom";
    public static final String TAXREF_NOM_FR = "nom_fr";
    public static final String TAXREF_NIVEAU = "nv";
    public static final String TAXREF_REGNE = "regne";
    public static final String TAXREF_CLASSE = "classe";
    public static final String TAXREF_ORDRE = "ordre";
    public static final String TAXREF_FAMILLE = "famille";

    public static final String USERS_TABLE_NAME = "Users";
    public static final String USERS_KEY = "_id";
    public static final String USERS_LOGIN = "login";
    public static final String USERS_MDP = "mdp";

    public static final String TAXREF_TABLE_CREATE = "CREATE TABLE " + TAXREF_TABLE_NAME + " (" +
            TAXREF_KEY + " INTEGER PRIMARY KEY, " +
            TAXREF_NOM + " TEXT NOT NULL, " +
            TAXREF_NOM_FR + " TEXT, " +
            TAXREF_NIVEAU + " INTEGER NOT NULL CHECK (0 <= " + TAXREF_NIVEAU + " AND "+ TAXREF_NIVEAU + " <= 7), " +
            TAXREF_REGNE + " TEXT, " +
            TAXREF_CLASSE + " TEXT, " +
            TAXREF_ORDRE + " TEXT, " +
            TAXREF_FAMILLE + " TEXT);";

    public static final String USERS_TABLE_CREATE = "CREATE TABLE " + USERS_TABLE_NAME + " (" +
            USERS_KEY + " INTEGER PRIMARY KEY, " +
            USERS_LOGIN + " TEXT NOT NULL, " +
            USERS_MDP + " TEXT NOT NULL, " +
            "CONSTRAINT login_unique UNIQUE (" + USERS_LOGIN + "));";

    public static final String TAXREF_TABLE_DROP = "DROP TABLE IF EXISTS " + TAXREF_TABLE_NAME + ";";
    public static final String CREATE_INDEX_NOM = "CREATE INDEX index_nom ON " + TAXREF_TABLE_NAME + "(" + TAXREF_NOM + ");";
    public static final String CREATE_INDEX_NOM_FR = "CREATE INDEX index_nom_fr ON " + TAXREF_TABLE_NAME + "(" + TAXREF_NOM_FR + ");";

    public static final String USERS_TABLE_DROP = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME + ";";

    public TaxUsrDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TAXREF_TABLE_CREATE);
        db.execSQL(CREATE_INDEX_NOM);
        db.execSQL(CREATE_INDEX_NOM_FR);
        db.execSQL(USERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TAXREF_TABLE_DROP);
        db.execSQL(USERS_TABLE_DROP);
        onCreate(db);
    }
}
