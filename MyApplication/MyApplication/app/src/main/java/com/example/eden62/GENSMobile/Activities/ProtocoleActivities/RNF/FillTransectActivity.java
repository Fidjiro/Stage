package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Adapters.RNFAdapter.RNFInventaireAdapter;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;
import com.example.eden62.GENSMobile.Database.RNFDatabase.Transect;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;

public class FillTransectActivity extends ListActivity {

    private EditText filterLatText;
    private EditText filterFrText;
    RNFInventaireAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fill_transect);

        TextView titleView = (TextView) findViewById(R.id.titleViewTransect);
        filterLatText = (EditText) findViewById(R.id.search_box_lat);
        filterFrText = (EditText) findViewById(R.id.search_box_fr);
        filterLatText.addTextChangedListener(filterLatTextWatcher);
        filterFrText.addTextChangedListener(filterFrTextWatcher);
        Button valider = (Button) findViewById(R.id.valider);
        Button noObs = (Button) findViewById(R.id.noObs);

        Transect t = getIntent().getParcelableExtra("transect");

        titleView.setText(t.toString());

        adapter = new RNFInventaireAdapter(this,ChooseTransectActivity.transect1Inventories);

        setListAdapter(adapter);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.allDenombrementAreCoherent()) {
                    setResult(ChooseTransectActivity.RESULT_TRANSECT_DONE);
                    finish();
                } else
                    Toast.makeText(FillTransectActivity.this,"Le nombre d'individu mâles femelles est supérieur au total",Toast.LENGTH_SHORT).show();
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

    private TextWatcher filterLatTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getLatFilter().filter(s);
        }

    };

    private TextWatcher filterFrTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFrFilter().filter(s);
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterLatText.removeTextChangedListener(filterLatTextWatcher);
        filterFrText.removeTextChangedListener(filterFrTextWatcher);
    }
}
