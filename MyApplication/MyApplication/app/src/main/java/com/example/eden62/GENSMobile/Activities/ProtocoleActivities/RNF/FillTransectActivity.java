package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Adapters.RNFAdapter.RNFMatchableArrayAdapter;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventories;
import com.example.eden62.GENSMobile.Database.RNFDatabase.Transect;
import com.example.eden62.GENSMobile.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FillTransectActivity extends ListActivity {

    private static final int RESULT_TEMPS_PARCOURS = 7;
    private EditText filterLatText;
    private EditText filterFrText;
    private RNFMatchableArrayAdapter adapter = null;
    private RNFInventories recensedInvs;
    private ArrayList<RNFInventaire> currInvs;
    private Transect currTransect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fill_transect);

        TextView titleView = (TextView) findViewById(R.id.titleViewTransect);
        filterLatText = (EditText) findViewById(R.id.search_box_lat);
        filterFrText = (EditText) findViewById(R.id.search_box_fr);
        filterLatText.addTextChangedListener(filterLatTextWatcher);
        filterFrText.addTextChangedListener(filterFrTextWatcher);
        final Button valider = (Button) findViewById(R.id.valider);

        Intent callerIntent = getIntent();
        currTransect = callerIntent.getParcelableExtra("transect");

        titleView.setText(currTransect.toString());

        currInvs = new ArrayList<>(new TemplateRNFSpecies());
        if(callerIntent.getBooleanExtra("modif", false)){
            RNFInventories previousInventories = currTransect.getInventories();
            for(RNFInventaire inv : previousInventories){
                currInvs.set(currInvs.indexOf(inv),inv);
            }
        }

        setAdapter(currInvs);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FillTransectActivity.this);
                builder.setMessage(R.string.avertissementValidationTransect);
                builder.setTitle(getString(R.string.avertissement));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recensedInvs = getAllRecensedInventories(currInvs);
                        if(recensedInvs.size() == 0)
                            Toast.makeText(FillTransectActivity.this, "Aucune observation", Toast.LENGTH_LONG).show();
                        setAdapter(recensedInvs);
                        valider.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recensedInvs = getAllRecensedInventories(recensedInvs);
                                if(isTransectValidable()) {
                                    Intent intent = new Intent(FillTransectActivity.this, TakeTimeActivity.class);
                                    intent.putExtra("oldMinutes",currTransect.getMinutes());
                                    intent.putExtra("oldSecondes",currTransect.getSecondes());
                                    startActivityForResult(intent, RESULT_TEMPS_PARCOURS);
                                }
                                else
                                    actionWhenTransectNotValidable();
                            }
                        });
                    }
                });
                builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private boolean isTransectValidable(){
        return recensedInvs.allDenombrementAreCoherent() && recensedInvs.allNeededDenombrementAreNoted();
    }

    private void actionWhenTransectNotValidable(){
        if(!recensedInvs.allNeededDenombrementAreNoted())
            Toast.makeText(this, getString(R.string.denombrementNotNoted), Toast.LENGTH_LONG).show();
        if(!recensedInvs.allDenombrementAreCoherent())
            Toast.makeText(FillTransectActivity.this, getString(R.string.wrongDenombrementRNF), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Intent resultIntent = new Intent();

            // On ne peut pas stocker directement le RNFInventories dans l'intent donc on le stock en array list
            // Il contiendra donc uniquement les espèces recensés
            resultIntent.putParcelableArrayListExtra("recensedInvs",recensedInvs);
            resultIntent.putExtra("minutes",data.getIntExtra("minutes",0));
            resultIntent.putExtra("secondes",data.getIntExtra("secondes",0));

            setResult(ChooseTransectActivity.RESULT_TRANSECT_DONE, resultIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.avertissementCancelTransect);
        builder.setTitle(getString(R.string.avertissement));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FillTransectActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Récupère tous les inventaires qui ont été recensé dans la liste des inventaires disponible
     *
     * @return La liste des inventaires recensés
     */
    public RNFInventories getAllRecensedInventories(ArrayList<RNFInventaire> l){
        RNFInventories res = new RNFInventories();
        for(RNFInventaire inv : l){
            if(isRecensed(inv))
                res.add(inv);
        }
        return res;
    }

    // Retourne true si l'inventaire en paramètre à été recensé (que l'un de ses dénombrements soit supérieur à 0)
    private boolean isRecensed(RNFInventaire inv){
        return inv.getNombre() > 0 || inv.getNbMale() > 0 || inv.getNbFemale() > 0;
    }

    private TextWatcher filterLatTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) { adapter.setLatinFilterMode(true);
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }

    };

    private TextWatcher filterFrTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) { adapter.setLatinFilterMode(false);
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }

    };

    // Réinitialise l'adapter avec la liste items en paramètre
    private void setAdapter(ArrayList<RNFInventaire> items){
        adapter = new RNFMatchableArrayAdapter(this, R.layout.row_item_rnf_inventaires, items);

        setListAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterLatText.removeTextChangedListener(filterLatTextWatcher);
        filterFrText.removeTextChangedListener(filterFrTextWatcher);
    }
}
