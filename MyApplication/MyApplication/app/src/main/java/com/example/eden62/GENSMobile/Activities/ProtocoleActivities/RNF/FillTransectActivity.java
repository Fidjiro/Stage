package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventories;
import com.example.eden62.GENSMobile.Database.RNFDatabase.Transect;
import com.example.eden62.GENSMobile.R;

public class FillTransectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fill_transect);

        final TextView latin1 = (TextView) findViewById(R.id.nomLatin1);
        TextView latin2 = (TextView) findViewById(R.id.nomLatin2);
        TextView fr1 = (TextView) findViewById(R.id.nomFr1);
        TextView fr2 = (TextView) findViewById(R.id.nomFr2);
        Button valider = (Button) findViewById(R.id.valider);
        Button noObs = (Button) findViewById(R.id.noObs);

        Transect t = getIntent().getParcelableExtra("transect");
        final RNFInventories invs = t.getInventories();

        try {
            latin1.setText(invs.get(0).getNomLatin());
            latin2.setText(invs.get(1).getNomLatin());
            fr1.setText(invs.get(0).getNomFr());
            fr2.setText(invs.get(1).getNomFr());
        }catch (Exception e){}
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ChooseTransectActivity.RESULT_TRANSECT_DONE);
                finish();
            }
        });

        noObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ChooseTransectActivity.RESULT_TRANSECT_DONE);
                finish();
            }
        });
    }
}
