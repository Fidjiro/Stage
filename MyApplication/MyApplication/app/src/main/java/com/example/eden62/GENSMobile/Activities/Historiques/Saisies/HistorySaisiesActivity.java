package com.example.eden62.GENSMobile.Activities.Historiques.Saisies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.eden62.GENSMobile.Activities.Historiques.HistoryActivity;
import com.example.eden62.GENSMobile.Adapters.HistoryAdapters.SaisiesAdapter;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaireDao;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.util.List;

public class HistorySaisiesActivity extends HistoryActivity<SaisiesAdapter> {

    protected CampagneProtocolaireDao campagneProtocolaireDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setAdapter() {
        List<CampagneProtocolaire> campagnes = campagneProtocolaireDao.getCampagneOfTheUsr(Utils.getCurrUsrId(this));

        adapter = new SaisiesAdapter(this, campagnes);
        listItems.setAdapter(adapter);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_history_saisies);
    }

    @Override
    protected void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup)inflater.inflate(R.layout.row_item_protocole_campagne, listItems,false);
    }

    @Override
    protected void actionOnItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CampagneProtocolaire c = (CampagneProtocolaire)adapterView.getItemAtPosition(i);

        Intent intent = new Intent(this, ShowInfoSaisieActivity.class);
        intent.putExtra("campagne",c);
        startActivity(intent);
    }

    @Override
    protected void openDatabases() {
        campagneProtocolaireDao = new CampagneProtocolaireDao(this);
        campagneProtocolaireDao.open();
    }

    @Override
    protected void closeDatabases() {
        campagneProtocolaireDao.close();
    }
}
