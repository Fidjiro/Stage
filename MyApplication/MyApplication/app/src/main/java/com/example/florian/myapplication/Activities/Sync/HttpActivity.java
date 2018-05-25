package com.example.florian.myapplication.Activities.Sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.florian.myapplication.Database.CampagneDatabase.CampagneDAO;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * IN PROGRESS
 */
public class HttpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnHit;
    TextView txtJson;
    private Snackbar snackbar;

    private CookieJar cookieJar;
    private OkHttpClient client;
    private String username;
    private long usrId;
    static final String URL = "http://vps122669.ovh.net:8080/connexion.php";

    private CampagneDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        dao = new CampagneDAO(this);
        dao.open();

        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        makeView();

        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = loginPreferences.getString("username","");
        usrId = loginPreferences.getLong("usrId" , 0);

        snackbar = Snackbar.make(txtJson, "Requête en cours d'exécution",
                Snackbar.LENGTH_INDEFINITE);
    }

    private void makeView() {
        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);
        btnHit.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        if (!isConnected()) {
            Snackbar.make(view, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
            return;
        }
        snackbar.show();

        System.out.println("Log: " + username );
        RequestBody requestBody = new FormBody.Builder().add("log",username).build();
        final Request request = new Request.Builder().url(URL).post(requestBody).build();
        Log.e("Requête",request.toString());



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException(response.toString());
                    }

                    final String body = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtJson.setText(body);
                            snackbar.dismiss();
                        }
                    });

                    JSONObject js = parseStringToJsonObject(body);
                    interpreteConnexionByJson(js);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    private RequestBody createRequestBodyToSend(){
        Inventaire inv = dao.getInventaireOfTheUsr(usrId);

        RequestBody requestBody = new FormBody.Builder().add("_id",inv.get_id() + "").
                                                        add("ref_taxon",inv.getRef_taxon() + "").
                                                        add("ref_user",inv.getUser() + "").
                                                        add("typeTaxon",inv.getTypeTaxon() + "").
                                                        add("latitude",inv.getLatitude() + "").
                                                        add("longitude",inv.getLongitude() + "").
                                                        add("date",inv.getDate()).
                                                        add("nombre",inv.getNombre() + "").
                                                        add("type_obs",inv.getType_obs()).
                                                        add("nbMale",inv.getNbMale() + "").
                                                        add("nbFemale",inv.getNbFemale() + "").
                                                        add("presencePonte",inv.getPresencePonte()).
                                                        add("activite",inv.getActivite()).
                                                        add("statut",inv.getStatut()).
                                                        add("nidif",inv.getNidif()).
                                                        add("indice_abondance",inv.getIndiceAbondance() + "").
                                                        build();
        return requestBody;
    }

    protected void interpreteConnexionByJson(JSONObject json){
        int err = -1;
        int con = -1;
        String titre = "";
        String msg = "";
        try {
            err = json.getInt("err");
            con = json.getInt("con");
            titre = json.getString("titre");
            msg = json.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(err == 0){
            if(con == 0){
                Log.w(titre,msg);
            }else if(con == 1){
                Log.w("Connexion","Connexion réussi");
                RequestBody requestBody = createRequestBodyToSend();
                final Request request = new Request.Builder().url(URL).post(requestBody).build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = client.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                throw new IOException(response.toString());
                            }

                            String body = response.body().string();
                            JSONObject json = parseStringToJsonObject(body);


                        }catch(IOException e){
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(txtJson, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }else {
                Log.w("Déconnexion", "Se déconnecte");
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected JSONObject parseStringToJsonObject(String s) throws JSONException {
        return new JSONObject(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
