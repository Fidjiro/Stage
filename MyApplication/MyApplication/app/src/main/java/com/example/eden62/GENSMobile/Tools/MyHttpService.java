package com.example.eden62.GENSMobile.Tools;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
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

public class MyHttpService {

    private Context ctx;
    private CookieJar cookieJar;
    private OkHttpClient client;

    static final String URL_CONNEXION = "http://vps122669.ovh.net:8080/connexion.php";
    static final String URL_ADD_DATA = "http://vps122669.ovh.net:8080/addData.php";
    static final String URL_INFO_CAMPAGNE = "http://vps122669.ovh.net:8080/InitSync.php";

    public MyHttpService(Context ctx) {
        this.ctx = ctx;
        this.cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ctx));
        this.client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    public Request createConnectionRequest(String login, String mdp){
        RequestBody requestBody = new FormBody.Builder().add("log",login).add("mdp",mdp).build();
        return new Request.Builder().url(URL_CONNEXION).post(requestBody).build();
    }

    public Request createSendDataRequest(Inventaire inv, int idCampagne){
        RequestBody requestBody = createRequestBodyToSend(inv, idCampagne);
        return new Request.Builder().url(URL_ADD_DATA).post(requestBody).build();
    }

    public Request createSendInfoCampagneRequest(int idCampagne, int nbInv){
        RequestBody requestBody = new FormBody.Builder().add("idCampagne",idCampagne + "").
                                                        add("nbInv", nbInv + "").
                                                        add("refUser",Utils.getCurrUsrId(ctx) + "").build();
        return new Request.Builder().url(URL_INFO_CAMPAGNE).post(requestBody).build();
    }

    public Response executeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    // Créé la requêtre à envoyer au serveur lors d'un envoi d'inventaire
    private RequestBody createRequestBodyToSend(Inventaire inv, int idCampagne){

        RequestBody requestBody = new FormBody.Builder().add("_id",inv.get_id() + "").
                add("ref_taxon",inv.getRef_taxon() + "").
                add("ref_user",inv.getUser() + "").
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

}
