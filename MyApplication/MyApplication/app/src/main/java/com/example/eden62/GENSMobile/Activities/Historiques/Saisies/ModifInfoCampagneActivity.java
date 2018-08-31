package com.example.eden62.GENSMobile.Activities.Historiques.Saisies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaireDao;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.HeureMinutesWatcher;
import com.example.eden62.GENSMobile.Tools.Utils;

/**
 * Activité permettant de modifier les informations d'une campagne (nom, heure début, heure fin)
 */
public class ModifInfoCampagneActivity extends AppCompatActivity {

    protected EditText heureDebut, minutesDebut, heureFin, minutesFin, nomCampagne;
    protected Button validerModif;

    protected long campagneId;
    protected CampagneProtocolaireDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modif_info_campagne);

        nomCampagne = (EditText) findViewById(R.id.nomRNF);
        heureDebut = (EditText) findViewById(R.id.heureDebutH);
        minutesDebut = (EditText) findViewById(R.id.heureDebutM);
        heureFin = (EditText) findViewById(R.id.heureFinH);
        minutesFin = (EditText) findViewById(R.id.heureFinM);
        validerModif = (Button) findViewById(R.id.valider);

        heureDebut.addTextChangedListener(new HeureMinutesWatcher(minutesDebut));
        minutesDebut.addTextChangedListener(new HeureMinutesWatcher(heureFin));
        heureFin.addTextChangedListener(new HeureMinutesWatcher(minutesFin));

        dao = new CampagneProtocolaireDao(this);
        dao.open();

        campagneId = getIntent().getLongExtra("campagneId",-1);
        final CampagneProtocolaire campagne = dao.getCampagneById(campagneId);

        String[] splittedHeureDebut = campagne.getHeureDebut().split(":");
        String[] splittedHeureFin = campagne.getHeureFin().split(":");
        heureDebut.setText(splittedHeureDebut[0]);
        minutesDebut.setText(splittedHeureDebut[1]);
        heureFin.setText(splittedHeureFin[0]);
        minutesFin.setText(splittedHeureFin[1]);
        nomCampagne.setText(campagne.getName());

        validerModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomCampagne.getText().toString();
                String heureDebH = heureDebut.getText().toString();
                String heureDebM = minutesDebut.getText().toString();
                String heureFH = heureFin.getText().toString();
                String heureFM = minutesFin.getText().toString();
                if(nom.isEmpty()){
                    nomCampagne.setError("Veuillez insérer un nom de campagne");
                    nomCampagne.requestFocus();
                    return;
                }
                if(notValidableHeure(heureDebH,heureDebM)) {
                    heureDebut.setError("Veuillez insérer une heure de début");
                    heureDebut.requestFocus();
                    return;
                }
                if(notValidableHeure(heureFH,heureFM)){
                    heureFin.setError("Veuillez insérer une heure de début");
                    heureFin.requestFocus();
                    return;
                }
                int hDebut = getIntFromString(heureDebH);
                int mDebut = getIntFromString(heureDebM);
                int hFin = getIntFromString(heureFH);
                int mFin = getIntFromString(heureFM);
                if(incoherentHeures(hDebut,mDebut,hFin,mFin)){
                    heureDebut.setError("Veuillez saisir une heure de début inférieur à celle de fin");
                    heureDebut.requestFocus();
                    return;
                }
                if(outOfBoundsHeure(hDebut)){
                    heureDebut.setError("Veuillez mettre une heure valide");
                    heureDebut.requestFocus();
                    return;
                } if(outOfBoundsHeure(hFin)){
                    heureFin.setError("Veuillez mettre une heure valide");
                    heureFin.requestFocus();
                    return;
                } if(outOfBoundsMinutes(mDebut)){
                    heureDebut.setError("Veuillez entrer des minutes valide");
                    heureDebut.requestFocus();
                    return;
                } if(outOfBoundsMinutes(mFin)){
                    heureFin.setError("Veuillez entrer des minutes valide");
                    heureFin.requestFocus();
                    return;
                }

                campagne.setName(nom);
                campagne.setHeureDebut(getHeureFromInts(hDebut,mDebut));
                campagne.setHeureFin(getHeureFromInts(hFin,mFin));
                dao.modifieCampagne(campagne);
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
