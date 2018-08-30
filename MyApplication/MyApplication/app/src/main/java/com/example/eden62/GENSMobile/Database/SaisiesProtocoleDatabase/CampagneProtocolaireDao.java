package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eden62.GENSMobile.Database.DAO;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFSaisie;

import java.util.ArrayList;
import java.util.List;

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
        return mDb.delete(TABLE_NAME, KEY + " = ?", new String[]{item.get_id() + ""});
    }

    @Override
    public void close() {
        mDb.close();
    }

    public long insertRNFCampagne(CampagneProtocolaire campagne){
        ContentValues cv = getCvFrom(campagne);
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

    public List<CampagneProtocolaire> getCampagneOfTheUsr(long usrId){
        Cursor c = selectCampagneOfTheUsr(usrId);

        return dealWithCursor(c);
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

    protected List<CampagneProtocolaire> dealWithCursor(Cursor c){
        List<CampagneProtocolaire> res = new ArrayList<>();

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
