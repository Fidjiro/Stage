package com.example.eden62.GENSMobile.Activities.Historiques.Inventaires;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.FormActivities.Faune.AmphibienActivity;
import com.example.eden62.GENSMobile.Activities.FormActivities.Faune.FauneActivity;
import com.example.eden62.GENSMobile.Activities.FormActivities.Faune.OiseauxActivity;
import com.example.eden62.GENSMobile.Activities.FormActivities.Flore.FloreActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.HistoryAdapters.InventaireAdapter;

import java.util.List;

public class HistoryRecensementActivity extends AppCompatActivity {

    protected ListView listInv;
    protected CampagneDAO campagneDao;
    protected TaxUsrDAO taxUsrDao;
    protected InventaireAdapter adapter;
    protected CheckBox changeAllCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_recensement);

        campagneDao = new CampagneDAO(this);
        taxUsrDao = new TaxUsrDAO(this);
        campagneDao.open();
        taxUsrDao.open();


        listInv = (ListView) findViewById(R.id.listViewRecensement);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.row_item_inventaires,listInv,false);
        changeAllCheckboxes = (CheckBox) header.findViewById(R.id.itemCheckbox);
        changeAllCheckboxes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    checkAll();
                else
                    uncheckAll();
            }
        });
        listInv.addHeaderView(header,null,false);
        listInv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nomsInvTxt = (TextView)view.findViewById(R.id.nomEspeceInv);
                TextView heureInvTxt = (TextView)view.findViewById(R.id.heureInventaire);

                String noms = nomsInvTxt.getText().toString();
                String[] splittedNoms;
                if(noms.contains(" - "))
                    splittedNoms = noms.split(" - ");
                else {
                    if(noms.contains(" sp."))
                        noms = noms.replace(" sp.","");
                    splittedNoms = new String[]{noms, ""};
                }
                String[] params = new String[] {splittedNoms[0],splittedNoms[1],heureInvTxt.getText().toString()};
                Inventaire selectedInventaire = campagneDao.getInventaireFromHistory(params);
                startActivity(generateGoodIntent(selectedInventaire));
            }
        });

        Button deleteSelection = (Button) findViewById(R.id.deleteSelect);
        deleteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });
    }

    protected Intent generateGoodIntent(Inventaire inv){
        Intent intent;
        if(usrInputIsPlantae(inv))
            intent = new Intent(this, FloreActivity.class);
        else if(usrInputIsAves(inv))
            intent = new Intent(this, OiseauxActivity.class);
        else if(usrInputIsAmphibia(inv))
            intent = new Intent(this, AmphibienActivity.class);
        else
            intent = new Intent(this, FauneActivity.class);
        intent.putExtra("selectedInv",inv);
        return intent;
    }
       /** Vérifie si l'espèce inséré par l'utilisateur est une plante
       * @return <code>True</code> si oui, <code>false</code> sinon
       */
        protected boolean usrInputIsPlantae(Inventaire inv){
            String regne = taxUsrDao.getRegne(new String[]{inv.getNomLatin(),inv.getNomFr()});
            return regne.equals(getString(R.string.plantae));
        }

        /**
         * Vérifie si l'espèce inséré par l'utilisateur est un oiseau
         * @return <code>True</code> si oui, <code>false</code> sinon
        */
        protected boolean usrInputIsAves(Inventaire inv){
            String classe = taxUsrDao.getClasse(new String[]{inv.getNomLatin(),inv.getNomFr()});
            return classe.equals(getString(R.string.aves));
        }

        /**
         * Vérifie si l'espèce inséré par l'utilisateur est un amphibien
         * @return <code>True</code> si oui, <code>false</code> sinon
         */
        protected boolean usrInputIsAmphibia(Inventaire inv){
            String classe = taxUsrDao.getClasse(new String[]{inv.getNomLatin(),inv.getNomFr()});
            return classe.equals(getString(R.string.amphibia));
        }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
        changeAllCheckboxes.setChecked(false);
    }

    private void setAdapter(){
        List<Inventaire> inventaires = campagneDao.getInventairesOfTheUsr(getCurrentUsrId());

        adapter = new InventaireAdapter(this,inventaires);
        listInv.setAdapter(adapter);
    }

    protected long getCurrentUsrId(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return loginPreferences.getLong("usrId",0);
    }

    public Dialog createDialog() {
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.avertissementDeleteSelection);
        builder.setTitle(getString(R.string.avertissement));
        builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCheckedItems();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        box = builder.create();
        return box;
    }

    private void deleteCheckedItems(){
        //RemoveCheckedItemsFromAdapter doit être appelé avant deleteCheckedItemsFromDao car cette dernière delete les inventaires
        //de la liste.
        adapter.removeCheckedItemsFromAdapter();
        adapter.getCheckedInventairesStocker().deleteCheckedItemsFromDao();
    }

    private void changeAllCheckboxStatus(boolean checked){
        for(CheckBox cb : adapter.allCheckBoxes){
            cb.setChecked(checked);
        }
    }

    private void checkAll(){
        changeAllCheckboxStatus(true);
    }

    private void uncheckAll(){
        changeAllCheckboxStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campagneDao.close();
    }
}
