package com.example.florian.myapplication.Database.ReleveDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;

public class HistoryDao {

    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String NAME = "database2.db";

    protected SQLiteDatabase mDb;
    protected HistoryDatabaseHandler mHandler;

    public static final String TABLE_NAME = "History";
    public static final String KEY = "_id";
    public static final String CREATOR = "creator";
    public static final String NOM = "nom";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String TIME = "heure";

    public HistoryDao(Context pContext){
        this.mHandler = new HistoryDatabaseHandler(pContext, NAME, null, VERSION);
    }

    /**
     * Ouvre la base de donnée
     */
    public void open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
    }

    /**
     * Ferme la base de données
     */
    public void close() {
        mDb.close();
    }

    /**
     * Ajout un relevé à la base
     *
     * @param rel Le Releve à ajouter
     * @return Le résultat de l'insertion du relevé dans la base
     */
    public long insertInventaire(Releve rel){
        ContentValues cv = getCvFrom(rel);
        return mDb.insert(TABLE_NAME,null,cv);
    }

    private ContentValues getCvFrom(Releve rel){
        ContentValues cv = new ContentValues();
        cv.put(KEY,rel.get_id());
        cv.put(CREATOR,rel.getCreator());
        cv.put(NOM,rel.getNom());
        cv.put(TYPE,rel.getType());
        cv.put(DATE,rel.getDate());
        cv.put(TIME,rel.getHeure());
        return cv;
    }

}
