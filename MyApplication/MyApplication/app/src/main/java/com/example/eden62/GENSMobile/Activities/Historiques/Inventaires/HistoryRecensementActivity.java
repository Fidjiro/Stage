package com.example.eden62.GENSMobile.Activities.Historiques.Inventaires;

import android.content.Intent;
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
import com.example.eden62.GENSMobile.Activities.Historiques.HistoryActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.HistoryAdapters.InventaireAdapter;

import java.util.List;

public class HistoryRecensementActivity extends HistoryActivity<InventaireAdapter> {

    protected CampagneDAO campagneDao;
    protected TaxUsrDAO taxUsrDao;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_history_recensement);
    }

    @Override
    protected void openDatabases() {
        campagneDao = new CampagneDAO(this);
        taxUsrDao = new TaxUsrDAO(this);
        campagneDao.open();
        taxUsrDao.open();
    }

    /**
     * Lance le bon type de formulaire en fonction de l'inventaire
     * @param inv L'inventaire à consulter
     * @return L'intent du bon type de formulaire
     */
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
    protected void actionOnItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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

    @Override
    protected void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup)inflater.inflate(R.layout.row_item_inventaires,listItems,false);
    }

    @Override
    protected void setAdapter(){
        List<Inventaire> inventaires = campagneDao.getInventairesOfTheUsr(getCurrentUsrId());

        adapter = new InventaireAdapter(this,inventaires);
        listItems.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campagneDao.close();
        taxUsrDao.close();
    }
}
