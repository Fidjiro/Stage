package com.example.eden62.GENSMobile.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.User;
import com.example.eden62.GENSMobile.Parser.CsvToSQLite.TaxRefParser;
import com.example.eden62.GENSMobile.Parser.CsvToSQLite.UserParser;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.MyHttpService;
import com.example.eden62.GENSMobile.Tools.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Activité login qui permet de se log via le login de l'utilisateur
 */
public class LoginActivity extends AppCompatActivity {

    private TaxUsrDAO dao;
    private MyHttpService httpService;
    private Button majUsr;
    private TextView txtJson;

    /**
     * Garde en mémoiré l'état de la connection pour s'assurer qu'on puisse l'arrêter si c'est demandé.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mLoginView;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private ProgressDialog majUsrInProcessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int verCode = Utils.getVerCode(this);
        loginPrefsEditor = loginPreferences.edit();

        // Si l'id de campagne n'est pas encore défini
        if(loginPreferences.getInt("idCampagne",-1) == -1) {
            loginPrefsEditor.putInt("idCampagne", 0);
            loginPrefsEditor.commit();
        }

        // Si la goodAppVersion n'est pas encore défini
        if(loginPreferences.getInt("goodAppVersion",-1) == -1) {
            loginPrefsEditor.putInt("goodAppVersion", verCode);
            loginPrefsEditor.commit();
        }
        checkVersion(verCode);

        httpService = new MyHttpService(this);
        dao = new TaxUsrDAO(this);
        dao.open();
        loadData();

        // Set up the login form.
        mLoginView = (EditText) findViewById(R.id.login);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        majUsr = (Button) findViewById(R.id.majUsrButton);

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        majUsr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                majUsrInProcessDialog = ProgressDialog.show(LoginActivity.this,"","Mise à jour en cours", true);
                majUsrInProcessDialog.setCancelable(false);
                MajUsrTask task = new MajUsrTask(httpService.createUpdateUsrListRequest());
                task.execute((Void)null);
            }
        });
    }

    /*Premier if: si la version de l'appli est inférieure à celle stockée ( via requête au serveur) on bloque
      Second if: si la version de l'application est plus grande que celle stockée (l'utilisateur à mis a jour l'application
      sans passer par la vérification avant la synchro) on met à jour la version stockée
     */
    private void checkVersion(int currentVersion){
        int registeredVersion = loginPreferences.getInt("goodAppVersion",-1);
        if(currentVersion < registeredVersion){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.updateApp);
            builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }else if(currentVersion > registeredVersion){
            loginPrefsEditor.putInt("goodAppVersion", currentVersion);
            loginPrefsEditor.commit();
        }
    }

    /**
     * Créer un fichier vide pour vérifier si les bases ont été chargé ou non
     *
     * @param filename Le nom du fichier à créer
     * @param content Le contenu du fichier
     */
    private void createNewFile(String filename, String content){
       FileOutputStream outputStream;
       try {
           outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
           outputStream.write(content.getBytes());
           outputStream.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    /**
     * Vérifie si le chargement des bases de données à déjà été réalisé
     *
     * @param filename Le nom du fichier de vérification
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    private boolean loadComplete(String filename){
        return new File(this.getFilesDir(),filename).exists();
    }

    /**
     * Charge les bases de données Taxref et Utilisateur
     */
    private void loadData() {
        String filename = "loadComplete.txt";
        if(!loadComplete(filename)){
            String fileContents = "toto";
            UserParser parserUsr = new UserParser(this);
            parserUsr.parseCSVFileToDb(dao);
            TaxRefParser parserTax = new TaxRefParser(this);
            parserTax.parseCSVFileToDb(dao);
            createNewFile(filename, fileContents);
        }
    }

    /**
     * Tente de connecter l'utilisateur via le login/mdp qu'il a fournit.
     * Si le couple ne correspond pas (pas dans la base, champ manquant, etc.),
     * un message d'erreur est affiché et aucune tentative de conenction est réalisée
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid login.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_incorrect_login));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(login);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isLoginValid(String email) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    /**
     * Représente une tâche asynchrone qui permet de connecter l'utilisateur
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mLogin;

        UserLoginTask(String login) {
            mLogin = login;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor c = dao.checkUsrValid(new String[] {mLogin});
            return c.moveToNext();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                rememberUsrId();
                Utils.hideKeyboard(getApplicationContext(),getCurrentFocus());
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                mLoginView.setError(getString(R.string.error_incorrect_login));
                mLoginView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        private void rememberUsrId(){
            long usrId = dao.getUsrId(new String[]{mLogin});
            loginPrefsEditor.putString("username", mLogin);
            loginPrefsEditor.putLong("usrId",usrId);
            loginPrefsEditor.commit();
        }
    }

    /**
     * Tâche qui permet de mettre à jour la liste d'utilisateur
     */
    private class MajUsrTask extends AsyncTask<Void,Void,Boolean>{

        private final Request mRequete;
        private String errMsg;
        private JSONArray users;
        private int nbUsers;

        public MajUsrTask(Request requete) {
            mRequete = requete;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if (!Utils.isConnected(LoginActivity.this)) {
                    Snackbar.make(majUsr, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
                    return false;
                }

                Response response = httpService.executeRequest(mRequete);
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtJson.setText(body);
                    }
                });

                JSONObject js = parseStringToJsonObject(body);
                return interpreteJson(js);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(majUsr, "Mauvaise forme de json", Snackbar.LENGTH_LONG).show();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                long id;
                String login;
                User usrToAdd;
                //System.out.println("Old nb user : " + dao.getNbUsers());
                dao.clearUsers();
                for (int i = 0; i < nbUsers; ++i) {
                    try {
                        JSONObject user = users.optJSONObject(i);
                        id = user.getInt("id");
                        login = user.getString("login");
                        usrToAdd = new User(id,login);
                        dao.insertUsr(usrToAdd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                majUsrInProcessDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Mise à jour terminée",Toast.LENGTH_LONG).show();
                //System.out.println("Serv nb user: " + nbUsers + ", dao nb users: " + dao.getNbUsers());
            } else {
                if (errMsg != null) {
                    Snackbar.make(majUsr, errMsg, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(majUsr, "Erreur de mise à jour", Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        private boolean interpreteJson(JSONObject json){
            int err;
            try{
                err = json.getInt("err");
                if(err == 1) {
                    errMsg = json.getString("msg");
                    return false;
                }
                nbUsers = json.getInt("nb_users");
                users = json.getJSONArray("users");
            }catch (JSONException e){
                e.printStackTrace();
                errMsg = "Mauvais parsage JSON";
                return false;
            }
            return true;
        }
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

