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

/**
 * Activité permettant à l'utilisateur le temps qu'il a mis pour traverser un transect
 */
public class TakeTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_time);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        LinearLayout takeTimeLayout = (LinearLayout) findViewById(R.id.takeTimeLayout);
        final EditText minutes = (EditText) findViewById(R.id.minutes);
        final EditText secondes = (EditText) findViewById(R.id.secondes);
        Button valider = (Button) findViewById(R.id.valider);

        Intent callerIntent = getIntent();

        setGoodText(minutes, callerIntent.getIntExtra("oldMinutes",0));
        setGoodText(secondes, callerIntent.getIntExtra("oldSecondes",0));

        takeTimeLayout.getLayoutParams().width = (int) (width * 0.75);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMinutes = minutes.getText().toString();
                String inputSecondes = secondes.getText().toString();
                if(inputMinutes.isEmpty() && inputSecondes.isEmpty())
                    minutes.setError("Vous devez indiquer votre temps de parcours");
                else {
                    Intent intent = new Intent();
                    intent.putExtra("minutes", getIntFromString(inputMinutes));
                    intent.putExtra("secondes", getIntFromString(inputSecondes));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    // Affecte à l'editText l'entier i en text seulement s'il est supérieur à 0
    private void setGoodText(EditText et, int i){
        String text = "";
        if(i > 0)
            text += i;
        et.setText(text);
    }

    // Récupère un int depuis une string
    private int getIntFromString(String s){
        int res = 0;
        try{
            res = Integer.parseInt(s);
        }catch (Exception e){

        }
        return res;
    }
}
