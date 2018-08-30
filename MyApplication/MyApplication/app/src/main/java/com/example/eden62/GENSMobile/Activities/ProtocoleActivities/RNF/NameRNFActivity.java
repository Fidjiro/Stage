package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

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

    private boolean outOfBoundsHeure(int heure){
        return heure < 0 || heure > 23;
    }

    private boolean outOfBoundsMinutes(int minutes){
        return minutes < 0 || minutes > 59;
    }

    private boolean notValidableHeure(String heureDeb, String heureFin){
        return heureDeb.isEmpty() && heureFin.isEmpty();
    }

    private String getHeureFromInts(int debut, int fin){
        return Utils.formatInt(debut) + ":" + Utils.formatInt(fin);
    }

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
