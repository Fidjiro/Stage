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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    private final OkHttpClient client = new OkHttpClient();
    private String username;
    private String password;
    static final String URL = "http://vps122669.ovh.net:8080/index.php";///&log=%s&mdp=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
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
                    JSONObject js = parseStringToJsonObject(jsonDeco);
                    interpreteConnexionByJson(js);
                    Log.w("COUCOU", "Fin thread");
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

/*package com.example.florian.myapplication.Activities.Sync;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.florian.myapplication.R;

import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpActivity extends AppCompatActivity {

    Button btnHit;
    TextView txtJson;
    String username,password;
    ProgressDialog pd;
    static final String URL = "http://vps122669.ovh.net/connexion/mobileConnecter/&log=%s&mdp=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);

        final String realUrl = generateUrl();

        final List<BasicNameValuePair> valeursPOST = new ArrayList<>();
        valeursPOST.add(new BasicNameValuePair("username",username));
        valeursPOST.add(new BasicNameValuePair("password",password));

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostRequest maRequete = new PostRequest();
                maRequete.sendRequest(realUrl, valeursPOST); // Envoie la requête POST
                txtJson.setText(maRequete.getResultat());
                Log.w("Done","request");
            }
        });
    }

    private String generateUrl(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = loginPreferences.getString("username","");
        password = loginPreferences.getString("password","");
        String toto = String.format(URL,username,password);
        Log.w("url",toto);
        return toto;
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(HttpActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            txtJson.setText(result);
        }
    }
}
*/