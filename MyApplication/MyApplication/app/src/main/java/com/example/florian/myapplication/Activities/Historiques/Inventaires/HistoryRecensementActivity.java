package com.example.florian.myapplication.Activities.Historiques.Inventaires;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.FormActivities.Faune.AmphibienActivity;
import com.example.florian.myapplication.Activities.FormActivities.Faune.FauneActivity;
import com.example.florian.myapplication.Activities.FormActivities.Faune.OiseauxActivity;
import com.example.florian.myapplication.Activities.FormActivities.Flore.FloreActivity;
import com.example.florian.myapplication.Database.CampagneDatabase.CampagneDAO;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.Database.LoadingDatabase.TaxUsrDAO;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.HistoryAdapters.InventaireAdapter;

import java.util.List;

public class HistoryRecensementActivity extends AppCompatActivity {

    protected ListView listInv;
    protected CampagneDAO campagneDao;
    protected TaxUsrDAO taxUsrDao;
    protected InventaireAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_recensement);

        campagneDao = new CampagneDAO(this);
        taxUsrDao = new TaxUsrDAO(this);
        campagneDao.open();
        taxUsrDao.open();


        listInv = (ListView) findViewById(R.id.listViewRecensement);
        listInv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nomsInvTxt = (TextView)view.findViewById(R.id.nomEspeceInv);
                TextView dateInvTxt = (TextView)view.findViewById(R.id.dateInv);
                TextView heureInvTxt = (TextView)view.findViewById(R.id.heureInventaire);

                String noms = nomsInvTxt.getText().toString();
                String[] splittedNoms;
                if(noms.contains(" - "))
                    splittedNoms = noms.split(" - ");
                else {
                    if(noms.contains(" sp."))
                        noms.replace(" sp.","");
                    splittedNoms = new String[]{noms, ""};
                }
                String[] params = new String[] {splittedNoms[0],splittedNoms[1],dateInvTxt.getText().toString(),heureInvTxt.getText().toString()};
                Inventaire selectedInventaire = campagneDao.getInventaireFromHistory(params);
                startActivity(generateGoodIntent(selectedInventaire));
            }
        });

        Button deleteSelection = (Button) findViewById(R.id.deleteSelect);
        deleteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getCheckedInventairesStocker().deleteCheckedItemsFromDao();
                setAdapter();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campagneDao.close();
    }
}
