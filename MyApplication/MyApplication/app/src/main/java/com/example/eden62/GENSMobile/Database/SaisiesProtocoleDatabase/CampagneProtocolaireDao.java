package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eden62.GENSMobile.Database.DAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Méthodes de commnication avec la base de données des campagne protocolaires
 */
public class CampagneProtocolaireDao implements DAO<CampagneProtocolaire> {

    protected final int VERSION = 3;
    protected final static String NAME = "database4.db";

    protected SQLiteDatabase mDb;
    protected CampagneProtocolaireDatabaseHandler mHandler;

    public static final String TABLE_NAME = "RNF_campagnes";
    public static final String KEY = "_id";
    public static final String AUTHOR_ID = "author_id";
    public static final String NOM = "nom";
    public static final String DATE = "date";
    public static final String HEURE_DEBUT = "heure_debut";
    public static final String HEURE_FIN = "heure_fin";
    public static final String NOM_PROTO = "nom_proto";
    public static final String NOM_SITE = "nom_site";
    public static final String SAISIE = "saisie";

    public CampagneProtocolaireDao(Context pContext){
        this.mHandler = new CampagneProtocolaireDatabaseHandler(pContext, NAME, null, VERSION);
    }

    @Override
    public void open(){
        mDb = mHandler.getWritableDatabase();
    }

    @Override
    public long delete(CampagneProtocolaire item) {
        return delete(item.get_id());
    }

    public long delete(long _id){
        return mDb.delete(TABLE_NAME, KEY + " = ?", new String[]{_id + ""});
    }

    @Override
    public void close() {
        mDb.close();
    }

    public long insertCampagne(CampagneProtocolaire campagne){
        ContentValues cv = getCvFrom(campagne);
        return mDb.insert(TABLE_NAME, null, cv);
    }

    public long modifieCampagne(CampagneProtocolaire campagne){
        delete(campagne);
        ContentValues cv = getCvFrom(campagne);
        cv.put(KEY,campagne.get_id());
        return mDb.insert(TABLE_NAME, null, cv);
    }

    private ContentValues getCvFrom(CampagneProtocolaire campagne){
        ContentValues cv = new ContentValues();
        cv.put(AUTHOR_ID,campagne.getAuthor_id());
        cv.put(NOM,campagne.getName());
        cv.put(DATE,campagne.getDate());
        cv.put(HEURE_DEBUT,campagne.getHeureDebut());
        cv.put(HEURE_FIN,campagne.getHeureFin());
        cv.put(NOM_PROTO,campagne.getNomProto());
        cv.put(NOM_SITE,campagne.getNomSite());
        cv.put(SAISIE,campagne.getSaisie());
        return cv;
    }

    private Cursor selectCampagneOfTheUsr(long usrId){
        String request = "SELECT * FROM " + TABLE_NAME + " WHERE " + AUTHOR_ID + " = ?;";

        return mDb.rawQuery(request, new String[]{usrId + ""});
    }

    public ArrayList<CampagneProtocolaire> getCampagneOfTheUsr(long usrId){
        Cursor c = selectCampagneOfTheUsr(usrId);

        return dealWithCursor(c);
    }

    public int getNbCampagneOfTheUsr(long usrId){
        Cursor c = selectCampagneOfTheUsr(usrId);

        return c.getCount();
    }

    private Cursor selectCampagneById(long id){
        String request = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?;";

        return mDb.rawQuery(request, new String[]{id + ""});
    }

    public CampagneProtocolaire getCampagneById(long id){
        Cursor c = selectCampagneById(id);
        c.moveToNext();
        return dealWithSingleRowCursor(c);
    }

    private CampagneProtocolaire dealWithSingleRowCursor(Cursor c){
        long _id = c.getLong(c.getColumnIndex(KEY));
        long author_id = c.getLong(c.getColumnIndex(AUTHOR_ID));
        String nom  = c.getString(c.getColumnIndex(NOM));
        String date  = c.getString(c.getColumnIndex(DATE));
        String heureDebut  = c.getString(c.getColumnIndex(HEURE_DEBUT));
        String heureFin  = c.getString(c.getColumnIndex(HEURE_FIN));
        String nomProto = c.getString(c.getColumnIndex(NOM_PROTO));
        String nomSite = c.getString(c.getColumnIndex(NOM_SITE));
        String saisie = c.getString(c.getColumnIndex(SAISIE));

        return new CampagneProtocolaire(_id, author_id, nom, date, heureDebut, heureFin, nomProto, nomSite, saisie);
    }

    protected ArrayList<CampagneProtocolaire> dealWithCursor(Cursor c){
        ArrayList<CampagneProtocolaire> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                CampagneProtocolaire rel = dealWithSingleRowCursor(c);
                res.add(rel);
            } while(c.moveToNext());
        }

        c.close();
        return res;
    }
}
