package com.example.eden62.GENSMobile.Database.CampagneDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eden62.GENSMobile.Database.DAO;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la base de données qui contient la table des {@link Inventaire}
 */
public class CampagneDAO implements DAO<Inventaire> {

    protected final static int VERSION = 4;
    // Le nom du fichier qui représente ma base
    protected final static String NAME = "database2.db";

    protected SQLiteDatabase mDb;
    protected CampagneDatabaseHandler mHandler;

    public static final String CAMPAGNE = "Campagne";
    public static final String KEY = "_id";
    public static final String REF_USR = "ref_usr";
    public static final String REF_TAXON = "ref_taxon";
    public static final String NV_TAXON = "nv_taxon";
    public static final String NOM_FR = "nom_fr";
    public static final String NOM_LATIN = "nom_latin";
    public static final String TYPE_TAXON = "type_taxon";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DATE = "date";
    public static final String HEURE = "heure";
    public static final String NB = "nombre";
    public static final String TYPE_OBS = "type_obs";
    public static final String REMARQUES = "remarques";
    public static final String NBMALE = "nombre_mâle";
    public static final String NBFEMALE = "nombre_femelle";
    public static final String PRESENCE_PONTE = "presence_ponte";
    public static final String ACTIVITE = "activite";
    public static final String STATUT = "statut";
    public static final String NIDIF = "nidification";
    public static final String ABONDANCE = "indice_abondance";
    public static final String ERR = "err";

    public CampagneDAO(Context pContext) {
        this.mHandler = new CampagneDatabaseHandler(pContext, NAME, null, VERSION);
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
     * Ajout un inventaire à la base
     *
     * @param inv L'Inventaire à ajouter
     * @return Le résultat de l'insertion de l'inventaire dans la base
     */
    public long insertInventaire(Inventaire inv){
        ContentValues cv = getCvFrom(inv);
        return mDb.insert(CAMPAGNE,null,cv);
    }

    /**
     * Modifie un inventaire dans la base
     *
     * @param inv L'inventaire à modifier
     * @return Le résultate de la modification de l'inventaire
     */
    public long modifInventaire(Inventaire inv){
        deleteInventaire(inv.get_id());
        ContentValues cv = getCvFrom(inv);
        cv.put(KEY,inv.get_id());
        return mDb.insert(CAMPAGNE,null,cv);
    }

    /**
     * Supprime l'inventaire de la base
     *
     * @param inv L'inventaire à supprimer
     * @return Le résultat de la suppression de l'inventaire
     */
    public long delete(Inventaire inv){
        return mDb.delete(CAMPAGNE,KEY + " = ?",new String[]{inv.get_id() + ""});
    }

    /**
     * Supprime l'inventaire de la base
     *
     * @param invId L'id de l'inventaire à supprimer
     * @return Le résultat de la suppression de l'inventaire
     */
    public long deleteInventaire(long invId){
        return mDb.delete(CAMPAGNE,KEY + " = ?",new String[]{invId + ""});
    }

    /**
     * Récupère un inventaire depuis l'historique des inventaires
     *
     * @param params Les paramètres permettant d'identifier l'inventaire
     * @return L'inventaire correspondant aux paramètres
     */
    public Inventaire getInventaireFromHistory(String[] params){
        String request = "SELECT * FROM " + CAMPAGNE + " WHERE " + NOM_LATIN + " = ? AND " + NOM_FR + " = ? AND " + HEURE + " = ?;";
        Cursor c = mDb.rawQuery(request,params);
        c.moveToNext();

        long _id = c.getLong(c.getColumnIndex(KEY));
        long ref_taxon = c.getLong(c.getColumnIndex(REF_TAXON));
        int nv_taxon = c.getInt(c.getColumnIndex(NV_TAXON));
        long ref_usr = c.getLong(c.getColumnIndex(REF_USR));
        String nomFr = c.getString(c.getColumnIndex(NOM_FR));
        String nomLatin = c.getString(c.getColumnIndex(NOM_LATIN));
        int type_taxon = c.getInt(c.getColumnIndex(TYPE_TAXON));
        double latitude = c.getDouble(c.getColumnIndex(LATITUDE));
        double longitude = c.getDouble(c.getColumnIndex(LONGITUDE));
        String date = c.getString(c.getColumnIndex(DATE));
        String heure = c.getString(c.getColumnIndex(HEURE));
        int nb = c.getInt(c.getColumnIndex(NB));
        String type_obs = c.getString(c.getColumnIndex(TYPE_OBS));
        String remarques = c.getString(c.getColumnIndex(REMARQUES));
        int nbMale = c.getInt(c.getColumnIndex(NBMALE));
        int nbFemale = c.getInt(c.getColumnIndex(NBFEMALE));
        String presencePonte = c.getString(c.getColumnIndex(PRESENCE_PONTE));
        String activite = c.getString(c.getColumnIndex(ACTIVITE));
        String statut = c.getString(c.getColumnIndex(STATUT));
        String nidif = c.getString(c.getColumnIndex(NIDIF));
        int indiceAbondance = c.getInt(c.getColumnIndex(ABONDANCE));
        int err = c.getInt(c.getColumnIndex(ERR));

        return new Inventaire(_id,ref_taxon,nv_taxon,ref_usr, nomFr, nomLatin, type_taxon, latitude,longitude,date, heure, nb,type_obs,remarques,nbMale,nbFemale,presencePonte,activite,statut,nidif,indiceAbondance,err);
    }

    /**
     * Récupère un {@link ContentValues} depuis l'inventaire en paramètre
     *
     * @param inv L'inventaire qui permet de créer le {@link ContentValues}
     * @return Le {@link ContentValues} correspondant à l'inventaire en paramètre
     */
    private ContentValues getCvFrom(Inventaire inv){
        ContentValues cv = new ContentValues();
        cv.put(REF_TAXON,inv.getRef_taxon());
        cv.put(NV_TAXON,inv.getNv_taxon());
        cv.put(REF_USR,inv.getUser());
        cv.put(NOM_FR,inv.getNomFr());
        cv.put(NOM_LATIN,inv.getNomLatin());
        cv.put(TYPE_TAXON,inv.getTypeTaxon());
        cv.put(LATITUDE,inv.getLatitude());
        cv.put(LONGITUDE,inv.getLongitude());
        cv.put(DATE,inv.getDate());
        cv.put(HEURE,inv.getHeure());
        cv.put(TYPE_OBS,inv.getType_obs());
        cv.put(REMARQUES,inv.getRemarques());
        cv.put(NB,inv.getNombre());
        cv.put(NBMALE,inv.getNbMale());
        cv.put(NBFEMALE,inv.getNbFemale());
        cv.put(PRESENCE_PONTE,inv.getPresencePonte());
        cv.put(ACTIVITE,inv.getActivite());
        cv.put(STATUT,inv.getStatut());
        cv.put(NIDIF,inv.getNidif());
        cv.put(ABONDANCE,inv.getIndiceAbondance());
        cv.put(ERR,inv.getErr());
        return cv;
    }

    /**
     * Récupère tous les champs des inventaires présent dans la base puis renvoit le dernier de la liste
     *
     * @return Le dernier Inventaire présent dans la base
     * @param usrId
     */
    public List<Inventaire> getInventaireOfTheUsr(long usrId){
        Cursor c = selectInvOfTheUsr(usrId);

        return dealWihCursor(c);
    }

    // Récupère le curseur indexé sur le premier inventaire correspondant à l'utilisateur
    private Cursor selectInvOfTheUsr(long usrId){
        String request = "SELECT * FROM " + CAMPAGNE + " WHERE " + REF_USR + " = ?";
        return mDb.rawQuery(request,new String[]{usrId + ""});
    }

    /**
     * Récupère le nombre d'inventaires de l'utilisateur usrId
     * @param usrId L'id de l'utilisateur
     * @return Le nombre d'inventaire de l'utilisateur
     */
    public int getNbInventairesOfTheUsr(long usrId){
        Cursor c = selectInvOfTheUsr(usrId);

        return c.getCount();
    }

    /**
     * Récupère les inventaires de l'utilisateur
     * @param usrId L'id de l'utilisateur
     * @return La liste des inventaires de l'utilisateur
     */
    public List<Inventaire> getInventairesOfTheUsr(long usrId){
        Cursor c = selectInvOfTheUsr(usrId);

        return dealWihCursor(c);
    }

    //Récupère une liste d'inventaire via le curseur en paramètre
    private List<Inventaire> dealWihCursor(Cursor c){
        List<Inventaire> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                long _id = c.getLong(c.getColumnIndex(KEY));
                long ref_taxon = c.getLong(c.getColumnIndex(REF_TAXON));
                int nv_taxon = c.getInt(c.getColumnIndex(NV_TAXON));
                long ref_usr = c.getLong(c.getColumnIndex(REF_USR));
                String nomFr = c.getString(c.getColumnIndex(NOM_FR));
                String nomLatin = c.getString(c.getColumnIndex(NOM_LATIN));
                int type_taxon = c.getInt(c.getColumnIndex(TYPE_TAXON));
                double latitude = c.getDouble(c.getColumnIndex(LATITUDE));
                double longitude = c.getDouble(c.getColumnIndex(LONGITUDE));
                String date = c.getString(c.getColumnIndex(DATE));
                String heure = c.getString(c.getColumnIndex(HEURE));
                int nb = c.getInt(c.getColumnIndex(NB));
                String type_obs = c.getString(c.getColumnIndex(TYPE_OBS));
                String remarques = c.getString(c.getColumnIndex(REMARQUES));
                int nbMale = c.getInt(c.getColumnIndex(NBMALE));
                int nbFemale = c.getInt(c.getColumnIndex(NBFEMALE));
                String presencePonte = c.getString(c.getColumnIndex(PRESENCE_PONTE));
                String activite = c.getString(c.getColumnIndex(ACTIVITE));
                String statut = c.getString(c.getColumnIndex(STATUT));
                String nidif = c.getString(c.getColumnIndex(NIDIF));
                int indiceAbondance = c.getInt(c.getColumnIndex(ABONDANCE));
                int err = c.getInt(c.getColumnIndex(ERR));

                Inventaire tmp = new Inventaire(_id,ref_taxon,nv_taxon,ref_usr, nomFr, nomLatin, type_taxon, latitude,longitude,date, heure, nb,type_obs,remarques,nbMale,nbFemale,presencePonte,activite,statut,nidif,indiceAbondance,err);

                res.add(tmp);
            }while(c.moveToNext());
        }

        c.close();
        return res;
    }

    /**
     * Récupère la base de donneés
     * @return La base de données
     */
    public SQLiteDatabase getDb() {
        return mDb;
    }
}
