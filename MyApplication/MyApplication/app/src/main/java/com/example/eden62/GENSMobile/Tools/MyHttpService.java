package com.example.eden62.GENSMobile.Tools;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Regroupe les actions avec le serveur
 */
public class MyHttpService {

    private Context ctx;
    private CookieJar cookieJar;
    private OkHttpClient client;

    static final String URL_CONNEXION = "http://vps122669.ovh.net:8080/connexion.php";
    static final String URL_ADD_DATA = "http://vps122669.ovh.net:8080/addData.php";
    static final String URL_INFO_CAMPAGNE = "http://vps122669.ovh.net:8080/InitSync.php";
    static final String URL_MAJ_USR = "http://vps122669.ovh.net:8080/SyncUsers.php";
    static final String URL_SEND_PROTO = "http://vps122669.ovh.net:8080/addDataProto4.php";

    public MyHttpService(Context ctx) {
        this.ctx = ctx;
        this.cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ctx));
        this.client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    /**
     * Crée la requête http pour connecter l'utilisateur au serveur
     *
     * @param login Le login de l'utilisateur
     * @param mdp Le mot de passe de l'utilisateur
     * @return La requête POST à envoyer
     */
    public Request createConnectionRequest(String login, String mdp){
        RequestBody requestBody = new FormBody.Builder().add("log",login).add("mdp",mdp).build();
        return new Request.Builder().url(URL_CONNEXION).post(requestBody).build();
    }

    /**
     * Crée la requête http pour envoyer un inventaire au serveur
     *
     * @param inv L'inventaire à envoyer
     * @param idCampagne L'id de campagne de cet inventaire
     * @return La requête POST à envoyer
     */
    public Request createSendDataRequest(Inventaire inv, int idCampagne){
        RequestBody requestBody = createRequestBodyToSend(inv, idCampagne);
        return new Request.Builder().url(URL_ADD_DATA).post(requestBody).build();
    }

    /**
     * Crée la requête http pour envoyer une campagne protocolaire au serveur
     *
     * @param c La campagne à envoyer
     * @return La requête POST à envoyer
     */
    public Request createSendCampagneProtoRequest(CampagneProtocolaire c){
        RequestBody requestBody = createRequestBodyProtoToSend(c);
        return new Request.Builder().url(URL_SEND_PROTO).post(requestBody).build();
    }

    /**
     * Crée la requête http pour envoyer les informations de la campagne au serveur
     *
     * @param idCampagne L'id de la campagne
     * @param nbInv Le nombre d'inventaires dans cette campagne
     * @return La requête POST à envoyer
     */
    public Request createSendInfoCampagneRequest(int idCampagne, int nbInv){
        RequestBody requestBody = new FormBody.Builder().add("idCampagne",idCampagne + "").
                                                        add("nbInv", nbInv + "").
                                                        add("refUser",Utils.getCurrUsrId(ctx) + "").build();
        return new Request.Builder().url(URL_INFO_CAMPAGNE).post(requestBody).build();
    }

    /**
     * Crée la requête http pour connecter l'utilisateur au serveur
     *
     * @return La requête POST à envoyer
     */
    public Request createUpdateUsrListRequest(){
        return new Request.Builder().url(URL_MAJ_USR).build();
    }

    /**
     * Exécute la requête http en paramètre
     *
     * @param request La requête à exécuter
     * @return La réponse du serveur suite à l'exécution de la requête
     * @throws IOException si la requête n'a pas pu être exécutée
     */
    public Response executeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    // Créé la requête à envoyer au serveur lors d'un envoi d'inventaire
    private RequestBody createRequestBodyToSend(Inventaire inv, int idCampagne){

        RequestBody requestBody = new FormBody.Builder().add("_id",inv.get_id() + "").
                add("ref_taxon",inv.getRef_taxon() + "").
                add("author_id",inv.getUser() + "").
                add("typeTaxon",inv.getTypeTaxon() + "").
                add("latitude",inv.getLatitude() + "").
                add("longitude",inv.getLongitude() + "").
                add("date",inv.getDate()).
                add("heure",inv.getHeure()).
                add("nombre",inv.getNombre() + "").
                add("type_obs",inv.getType_obs()).
                add("remarques",inv.getRemarques()).
                add("nbMale",inv.getNbMale() + "").
                add("nbFemale",inv.getNbFemale() + "").
                add("presencePonte",inv.getPresencePonte()).
                add("activite",inv.getActivite()).
                add("statut",inv.getStatut()).
                add("nidif",inv.getNidif()).
                add("indice_abondance",inv.getIndiceAbondance() + "").
                add("idCampagne", idCampagne + "").
                build();
        return requestBody;
    }

    // Créé la requête à envoyer au serveurs lors d'un envoi d'une campagne protocolaire
    private RequestBody createRequestBodyProtoToSend(CampagneProtocolaire c) {
        RequestBody requestBody = new FormBody.Builder().add("_id",c.get_id() + "").
                add("refUser",c.getAuthor_id() + "").
                add("name",c.getName()).
                add("date",c.getDate()).
                add("heure_debut",c.getHeureDebut()).
                add("heure_fin",c.getHeureFin()).
                add("nom_proto",c.getNomProto()).
                add("nom_site",c.getNomSite()).
                add("saisie",c.getSaisie()).
                build();
        return requestBody;
    }

}
