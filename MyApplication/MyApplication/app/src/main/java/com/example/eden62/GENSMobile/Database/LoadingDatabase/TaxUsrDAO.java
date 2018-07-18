package com.example.eden62.GENSMobile.Database.LoadingDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Base de données contenant les tables Taxref et User
 */
public class TaxUsrDAO {

    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String NAME = "database.db";

    protected SQLiteDatabase mDb;
    protected TaxUsrDatabaseHandler mHandler;

    public static final String TAG = "dao.java";

    public static final String TAXREF = "Taxref";
    public static final String KEY = "_id";
    public static final String NOM = "nom";
    public static final String NOM_FR = "nom_fr";
    public static final String NIVEAU = "nv";
    public static final String REGNE = "regne";
    public static final String CLASSE = "classe";
    public static final String ORDRE = "ordre";
    public static final String FAMILLE = "famille";

    public static final String USERS = "Users";
    public static final String LOGIN = "login";

    public TaxUsrDAO(Context pContext) {
        this.mHandler = new TaxUsrDatabaseHandler(pContext, NAME, null, VERSION);
    }

    public void open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    //Partie User

    /**
     * Ajoute un {@link User} à la base
     *
     * @param usr L'utilisateur à ajouter
     * @return Le resultat de l'insertion de cet utilisateur dans la base
     */
    public long insertUsr(User usr){
        ContentValues cv = new ContentValues();
        cv.put(KEY,usr.get_id());
        cv.put(LOGIN,usr.getLogin());
        return mDb.insert(USERS,null,cv);
    }

    /**
     * Vérrifie si le couple login/mdp fourni en paramètre existe dans la base
     *
     * @param logMdp Un tableau avec un login et un mot de passe
     * @return Le cursor indexé avant la ligne de l'user défini par ce login mot de passe ou devant rien si le couple n'existe pas
     */
    public Cursor checkUsrValid(String[] logMdp){
        String request = "SELECT * FROM " + USERS + " WHERE " + LOGIN + " = ?";
        return mDb.rawQuery(request,logMdp);
    }

    /**
     * Sélectionne l'id de l'utilisateur défini par le couple login/mdp en paramètre
     *
     * @param logMdp Un tableau avec un login et un mot de passe
     * @return Le cursor indexé devant la ligne correspondante au login/mdp
     */
    public Cursor selectUsrId(String[] logMdp){
        String request = "SELECT " + KEY + " FROM " + USERS + " WHERE LOGIN = ?";
        return mDb.rawQuery(request,logMdp);
    }

    /**
     * Récupère l'id de l'utilisateur défini par le couple login/mdp en param via le curseur
     *
     * @param logMdp Un tableau avec un login et un mot de passe
     * @return L'id de l'utilisateur
     */
    public long getUsrId(String[] logMdp){
        Cursor c = selectUsrId(logMdp);
        c.moveToNext();
        return c.getLong(c.getColumnIndex(KEY));
    }

    public void clearUsers(){
        mHandler.clearUsers(mDb);
    }

    public int getNbUsers(){
        Cursor c = mDb.rawQuery("SELECT * FROM " + USERS,new String[]{});
        return c.getCount();
    }

    //Partie taxon

    /**
     * Ajoute un taxon dans la base
     *
     * @param t Le taxon à ajouter
     * @return Le resultat de l'insertion du taxon dans la base
     */
    public long insertTaxon(Taxon t){
        ContentValues cv = new ContentValues();
        cv.put(KEY,t.get_id());
        cv.put(NOM,t.getNom());
        cv.put(NOM_FR,t.getNom_fr());
        cv.put(NIVEAU,t.getNv());
        cv.put(REGNE,t.getRegne());
        cv.put(CLASSE,t.getClasse());
        cv.put(ORDRE,t.getOrdre());
        cv.put(FAMILLE,t.getFamille());
        return mDb.insert(TAXREF,null,cv);
    }

    /**
     * Sélectionne les nomfr et les nomLatin pouvant correspondre à l'usrInput
     *
     * @param usrInput L'input de l'utilisateur
     * @param langue La langue dans lequel est l'input de l'utilisateur
     * @return le curseur indexé avant la première valeur possible
     */
    private Cursor selectByNom(String usrInput,String langue){
        String[] tmp = usrInput.split(" ");
        Cursor res;
        String condition = "";
        tmp[0] += "%";
        String baseRequest = "SELECT " + NOM + "," + NOM_FR + "," + NIVEAU + " FROM " + TAXREF + " WHERE " + langue + " LIKE ?";
        if(tmp.length == 1) {
            res = getDb().rawQuery(baseRequest, tmp);
        } else{
            for(int i = 1; i < tmp.length; ++i) {
                tmp[i] = "%" + tmp[i] + "%";
                condition += " AND " + langue + " LIKE ?";
            }
            res = getDb().rawQuery(baseRequest + condition, tmp);
        }
        return res;
    }

    /**
     * Sélectionne les nomfr et les nomLatin pouvant correspondre à l'usrInput
     *
     * @param usrInput L'input de l'utilisateur
     * @see TaxUsrDAO#selectByNom(String, String) selectByNom avec comme langue le latin
     * @return le curseur indexé avant la première valeur possible
     */
    public Cursor selectByNomLatin(String usrInput){
        return selectByNom(usrInput,NOM);
    }

    /**
     * Sélectionne les nomfr et les nomLatin pouvant correspondre à l'usrInput
     *
     * @param usrInput L'input de l'utilisateur
     * @see TaxUsrDAO#selectByNom(String, String) selectByNom avec comme langue le latin
     * @return le curseur indexé avant la première valeur possible
     */
    public Cursor selectByNomFr(String usrInput){
        return selectByNom(usrInput,NOM_FR);
    }

    /**
     * Retourne la liste de taxon qui pourrait correspondre au searchTerm selectionné avec le latin
     *
     * @param searchTerm L'usrInput
     * @return Le tableau de taxon éligible au searchTerm
     */
    public Taxon[] readLatin(String searchTerm) {
        Cursor cursor = selectByNomLatin(searchTerm);
        return dealWithCursor(cursor);
    }

    /**
     * Retourne la liste de taxon qui pourrait correspondre au searchTerm selectionné avec le français
     *
     * @param searchTerm L'usrInput
     * @return Le tableau de taxon éligible au searchTerm
     */
    public Taxon[] readFr(String searchTerm){
        Cursor cursor = selectByNomFr(searchTerm);
        return dealWithCursor(cursor);
    }

    /**
     * Récupère un taxon par ses noms
     *
     * @param noms les nomFr et latin du taxon voulu
     * @return Le cursor indexé au taxon
     */
    private Cursor selectByNoms(String[] noms){
        String request = "SELECT * FROM " + TAXREF + " WHERE " + NOM + " = ? AND " + NOM_FR + " = ?";
        Cursor c = mDb.rawQuery(request,noms);
        c.moveToNext();
        return c;
    }

    /**
     * Récupère le niveau du taxon correspondant aux paramètres
     * @param noms Les noms du taxon
     * @return Le niveau du taxon
     */
    public int getNiveau(String[] noms){
        Cursor c = selectByNoms(noms);
        return c.getInt(c.getColumnIndex(NIVEAU));
    }

    /**
     * Récupère le regne d'un Taxon
     *
     * @param noms Les noms du taxon
     * @return Le regne du taxon concerné
     */
    public String getRegne(String[] noms){
        Cursor c = selectByNoms(noms);
        return c.getString(c.getColumnIndex(REGNE));
    }

    /**
     * Récupère la classe d'un Taxon
     *
     * @param noms Les noms du taxon
     * @return La classe du taxon concerné
     */
    public String getClasse(String[] noms){
        Cursor c = selectByNoms(noms);
        return c.getString(c.getColumnIndex(CLASSE));
    }

    /**
     * Récupère le ref_taxon de l'espèce ayant les noms en paramètre
     * @param noms nom latin et nom fr dans cet ordre
     * @return le ref_taxon de l'espèce
     */
    public long getRefTaxon(String[] noms){
        Cursor c = selectByNoms(noms);
        return c.getLong(c.getColumnIndex(KEY));
    }

    /**
     * Utilise le curseur pour générer un tableau de suggestion de noms
     *
     * @param cursor Le curseur obtenu par selectByNomFr ou Latin
     * @return La liste de solutions possible
     */
    protected Taxon[] dealWithCursor(Cursor cursor){
        int recCount = cursor.getCount();

        Taxon[] ObjectItemData = new Taxon[recCount];
        int x = 0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                String nom = cursor.getString(cursor.getColumnIndex(NOM));
                String nomFr = cursor.getString(cursor.getColumnIndex(NOM_FR));
                int nv = cursor.getInt(cursor.getColumnIndex(NIVEAU));

                Taxon myObject = new Taxon(0,nom,nomFr,nv);

                ObjectItemData[x] = myObject;

                x++;

            } while (cursor.moveToNext());
        }

        cursor.close();
        return ObjectItemData;
    }
}
