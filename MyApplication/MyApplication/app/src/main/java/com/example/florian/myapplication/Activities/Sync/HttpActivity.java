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

import com.example.florian.myapplication.R;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
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
    private String password;
    static final String URL = "http://vps122669.ovh.net:8080/connexion.php";///&log=%s&mdp=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        makeView();
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

        RequestBody requestBody = new FormBody.Builder().add("log","rv").add("mdp","rv!9#").build();
        final Request request = new Request.Builder().url(generateUrl()).post(requestBody).build();
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
                    String jsonErr = "{\"err\":0,\"con\":0,\"titre\":\"Erreur connexion\",\"msg\":\"N'a pas réussi à se connecter\"}";
                    String jsonWin = "{\"err\":0,\"con\":1}";
                    String jsonDeco = "{\"err\":0}";
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
            }else {
                Log.w("Déconnexion", "Se déconnecte");
            }
        }
    }

    private String generateUrl() {
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = loginPreferences.getString("username","");
        password = loginPreferences.getString("password","");
        String toto = String.format(URL,username,password);
        Log.w("url",toto);
        return toto;
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
}
