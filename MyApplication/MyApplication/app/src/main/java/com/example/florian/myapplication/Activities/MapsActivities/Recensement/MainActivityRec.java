package com.example.florian.myapplication.Activities.MapsActivities.Recensement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.florian.myapplication.Activities.FormActivities.Faune.AmphibienActivity;
import com.example.florian.myapplication.Activities.FormActivities.Faune.FauneActivity;
import com.example.florian.myapplication.Activities.FormActivities.Faune.OiseauxActivity;
import com.example.florian.myapplication.Activities.FormActivities.Flore.FloreActivity;
import com.example.florian.myapplication.Activities.MapsActivities.MainActivity;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomArrayAdapter;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomFrArrayAdapter;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomLatinArrayAdapter;
import com.example.florian.myapplication.AutoComplete.AutoCompleteListeners.CustomAutoCompleteTextFrChangedListener;
import com.example.florian.myapplication.AutoComplete.AutoCompleteListeners.CustomAutoCompleteTextLatinChangedListener;
import com.example.florian.myapplication.AutoComplete.CustomAutoCompleteView;
import com.example.florian.myapplication.Database.LoadingDatabase.Taxon;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;

/**
 * Activité carte pour le recensement d'espèces
 */
public class MainActivityRec extends MainActivity {

    protected Button mesRecensement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button recenser = (Button) findViewById(R.id.recenser);

        mesRecensement = (Button) findViewById(R.id.mesCampagnes);

        mesRecensement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRec.this,HistoryRecensementActivity.class);
                startActivity(intent);
            }
        });

        recenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRec.this,SearchTaxonPopup.class);
                intent.putExtra("latitude",getUsrLatLong().getLatitude());
                intent.putExtra("longitude",getUsrLatLong().getLongitude());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main_rec);
    }

    @Override
    protected void setRelocButton(View.OnClickListener listener) {
        Button reloc1 = (Button) findViewById(R.id.reloc1);
        reloc1.setOnClickListener(listener);
    }

    protected void displayLayout(){
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.missionLayout);
        layout.setVisibility(View.VISIBLE);
    }
}