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
import com.kitfox.svg.Line;

public class NameRNFActivity extends AppCompatActivity {

    protected LinearLayout nameRnfLayout;
    protected EditText name;
    protected Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_rnf);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        nameRnfLayout = (LinearLayout) findViewById(R.id.nameRNFLayout);
        name = (EditText) findViewById(R.id.nomRNF);
        valider = (Button) findViewById(R.id.valider);

        nameRnfLayout.getLayoutParams().width = (int) (width*0.75);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = name.getText().toString();
                if(nom.isEmpty())
                    name.setError("Veuillez insérer un nom de campagne");
                else{
                    Intent intent = new Intent();
                    intent.putExtra("nomRnf",nom);
                    setResult(ChooseTransectActivity.RESULT_NAME_RNF,intent);
                    finish();
                }
            }
        });
    }
}
