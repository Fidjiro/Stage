package com.example.florian.myapplication.Database.ReleveDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;

import java.util.ArrayList;
import java.util.List;

public class HistoryDao {

    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String NAME = "database3.db";

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
        cv.put(CREATOR,rel.getCreator());
        cv.put(NOM,rel.getNom());
        cv.put(TYPE,rel.getType());
        cv.put(DATE,rel.getDate());
        cv.put(TIME,rel.getHeure());
        return cv;
    }

    public List<Releve> getReleveOfTheUsr(long creatorId){
        String request = "SELECT * FROM " + TABLE_NAME + " WHERE " + CREATOR + " = ?;";
        Cursor c = mDb.rawQuery(request,new String[]{creatorId +""});
        return dealWithCursor(c);
    }

    protected List<Releve> dealWithCursor(Cursor c){
        List<Releve> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                long _id = c.getLong(c.getColumnIndex(KEY));
                long creator = c.getLong(c.getColumnIndex(CREATOR));
                String nom = c.getString(c.getColumnIndex(NOM));
                String type = c.getString(c.getColumnIndex(TYPE));
                String date = c.getString(c.getColumnIndex(DATE));
                String heure = c.getString(c.getColumnIndex(TIME));

                Releve rel = new Releve(_id,creator,nom,type,date,heure);

                res.add(rel);
            } while(c.moveToNext());
        }

        c.close();
        return res;
    }

}
