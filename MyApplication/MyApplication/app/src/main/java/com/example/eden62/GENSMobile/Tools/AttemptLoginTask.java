package com.example.eden62.GENSMobile.Tools;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public abstract class AttemptLoginTask extends AsyncTask<Void,Void,Boolean> {

    protected final Request mRequete;
    protected MyHttpService httpService;
    protected int version;

    public AttemptLoginTask(Request requete, MyHttpService httpService) {
        mRequete = requete;
        this.httpService = httpService;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Response response = httpService.executeRequest(mRequete);
            if (!response.isSuccessful()) {
                throw new IOException(response.toString());
            }

            final String body = response.body().string();

            updateTxtJson(body);

            JSONObject js = parseStringToJsonObject(body);
            return interpreteJson(js);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            makeWrongJsonSnackbar();
        } return false;
    }

    /**
     * Affiche une snackbar avertissant du maivais parsage de json
     */
    protected abstract void makeWrongJsonSnackbar();

    /**
     * Change le texte de la textView qui affiche les résultats de requête
     *
     * @param body Le nouveau résultat de requête
     */
    protected abstract void updateTxtJson(final String body);

    @Override
    protected void onPostExecute(Boolean success) {
        actionOnPostExecute(success);
    }

    /**
     * Action réalisée après exécution de la tâche en arrière plan, propre à chaque tâche
     *
     * @param aBoolean Boolean représentant la réussite ou non de l'action en arrière plan
     */
    protected abstract void actionOnPostExecute(Boolean aBoolean);

    /**
     * Interprète le json reçu pour savoir si la connexion s'est bien passée ou non
     *
     * @param json Le json obtenu suite à la requête Http
     * @return <code>True</code> si l'utilisateur s'est conecté, <code>false</code> sinon
     */
    protected boolean interpreteJson(JSONObject json){
        int err = -1;
        int con = -1;
        version = -1;
        String titre = "";
        String msg = "";
        try {
            err = json.getInt("err");
            version = json.getInt("version");
            con = json.getInt("con");
            titre = json.getString("titre");
            msg = json.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(err == 0){
            if(con == 0){
                Log.w(titre,msg);
                return false;
            }else if(con == 1){
                Log.w("Connexion","Connexion réussi");
                return true;
            }else {
                Log.w("Déconnexion", "Se déconnecte");
                return false;
            }
        }return false;
    }

    /**
     * Transforme une chaîne de caractère en un objet JSON
     *
     * @param s La String à transformer
     * @return L'objet JSON correspondant à la String
     * @throws JSONException En cas d'echec de parsage
     */
    protected JSONObject parseStringToJsonObject(String s) throws JSONException {
        return new JSONObject(s);
    }
}
