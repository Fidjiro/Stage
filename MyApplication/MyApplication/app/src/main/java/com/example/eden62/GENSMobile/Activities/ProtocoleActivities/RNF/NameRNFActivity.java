package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.HeureMinutesWatcher;
import com.example.eden62.GENSMobile.Tools.Utils;

/**
 * Activité permettant de nommer une campagne rnf ainsi que de renseigner ses heures de début et fin
 */
public class NameRNFActivity extends AppCompatActivity {

    protected LinearLayout nameRnfLayout;
    protected EditText name, heureDebutH, heureDebutM, heureFinH, heureFinM;
    protected Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_rnf);

        final DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        nameRnfLayout = (LinearLayout) findViewById(R.id.nameRNFLayout);
        name = (EditText) findViewById(R.id.nomRNF);
        heureDebutH = (EditText) findViewById(R.id.heureDebutH);
        heureDebutM = (EditText) findViewById(R.id.heureDebutM);
        heureFinH = (EditText) findViewById(R.id.heureFinH);
        heureFinM = (EditText) findViewById(R.id.heureFinM);
        valider = (Button) findViewById(R.id.valider);

        heureDebutH.addTextChangedListener(new HeureMinutesWatcher(heureDebutM));
        heureDebutM.addTextChangedListener(new HeureMinutesWatcher(heureFinH));
        heureFinH.addTextChangedListener(new HeureMinutesWatcher(heureFinM));

        nameRnfLayout.getLayoutParams().width = (int) (width*0.75);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = name.getText().toString();
                String heureDebH = heureDebutH.getText().toString();
                String heureDebM = heureDebutM.getText().toString();
                String heureFH = heureFinH.getText().toString();
                String heureFM = heureFinM.getText().toString();
                if(nom.isEmpty()){
                    name.setError("Veuillez insérer un nom de campagne");
                    name.requestFocus();
                    return;
                }
                if(notValidableHeure(heureDebH,heureDebM)) {
                    heureDebutH.setError("Veuillez insérer une heure de début");
                    heureDebutH.requestFocus();
                    return;
                }
                if(notValidableHeure(heureFH,heureFM)){
                    heureFinH.setError("Veuillez insérer une heure de début");
                    heureFinH.requestFocus();
                    return;
                }
                int hDebut = getIntFromString(heureDebH);
                int mDebut = getIntFromString(heureDebM);
                int hFin = getIntFromString(heureFH);
                int mFin = getIntFromString(heureFM);

                if(incoherentHeures(hDebut,mDebut,hFin,mFin)){
                    heureDebutH.setError("Veuillez saisir une heure de début inférieur à celle de fin");
                    heureDebutH.requestFocus();
                    return;
                }
                if(outOfBoundsHeure(hDebut)){
                    heureDebutH.setError("Veuillez mettre une heure valide");
                    heureDebutH.requestFocus();
                    return;
                } if(outOfBoundsHeure(hFin)){
                    heureFinH.setError("Veuillez mettre une heure valide");
                    heureFinH.requestFocus();
                    return;
                } if(outOfBoundsMinutes(mDebut)){
                    heureDebutM.setError("Veuillez entrer des minutes valide");
                    heureDebutM.requestFocus();
                    return;
                } if(outOfBoundsMinutes(mFin)){
                    heureFinM.setError("Veuillez entrer des minutes valide");
                    heureFinM.requestFocus();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("nomRnf",nom);
                intent.putExtra("heureDebut", getHeureFromInts(hDebut,mDebut));
                intent.putExtra("heureFin", getHeureFromInts(hFin,mFin));
                setResult(ChooseTransectActivity.RESULT_NAME_RNF,intent);
                finish();
            }
        });
    }

    /**
     * Vérifie que l'heure du début est bien postérieure à l'heure de fin
     *
     * @param heureD L'heure de début
     * @param minutesD Les minutes de début
     * @param heureF L'heure de fin
     * @param minutesF les minutes de fin
     * @return <code>True</code> si l'heure de début est bien postérieure à l'heure de fin, <code>false</code> sinon
     */
    private boolean incoherentHeures(int heureD,int minutesD, int heureF, int minutesF){
        if(heureD == heureF)
            return minutesD > minutesF;
        return heureD > heureF;
    }

    /**
     * Vérifie que l'heure est bien sous forme 24h
     *
     * @param heure L'heure à vérifier
     * @return <code>True</code> si l'heure est du bon format, <code>false</code> sinon
     */
    private boolean outOfBoundsHeure(int heure){
        return heure < 0 || heure > 23;
    }

    /**
     * Vérifie que les minutes sont bien sous forme 60min
     *
     * @param minutes L'heure à vérifier
     * @return <code>True</code> si les minutes sont du bon format, <code>false</code> sinon
     */
    private boolean outOfBoundsMinutes(int minutes){
        return minutes < 0 || minutes > 59;
    }

    /**
     * Verifie qu'une heure est valide en vérifiant que soit l'heure soit les minutes sont renseignées
     *
     * @param heure L'heure à vérifier
     * @param minutes Les minutes à vérifier
     * @return <code>True</code> si l'heure global est validable, <code>false</code> sinon
     */
    private boolean notValidableHeure(String heure, String minutes){
        return heure.isEmpty() && minutes.isEmpty();
    }

    /**
     * Retourne une heure au format voulue
     *
     * @param heure Les heures de cette heure globale
     * @param minutes Les minutes de cette heure globale
     * @return L'heure global au bon format
     */
    private String getHeureFromInts(int heure, int minutes){
        return Utils.formatInt(heure) + ":" + Utils.formatInt(minutes);
    }

    /**
     * Récupère l'entier qui est sous forme de string
     *
     * @param s La string représentant l'entier
     * @return L'entier qui était en string
     */
    private int getIntFromString(String s){
        int res = 0;
        try{
            res = Integer.parseInt(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
