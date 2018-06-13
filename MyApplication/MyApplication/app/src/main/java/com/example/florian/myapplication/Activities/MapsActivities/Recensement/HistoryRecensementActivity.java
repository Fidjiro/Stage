package com.example.florian.myapplication.Activities.MapsActivities.Recensement;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.florian.myapplication.Database.CampagneDatabase.CampagneDAO;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.InventaireAdapter;

import java.util.List;

public class HistoryRecensementActivity extends AppCompatActivity {

    protected ListView listInv;
    protected CampagneDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_recensement);

        dao = new CampagneDAO(this);
        dao.open();

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
                else
                    splittedNoms = new String[]{noms,"*"};
                String[] params = new String[] {splittedNoms[0],splittedNoms[1],dateInvTxt.getText().toString(),heureInvTxt.getText().toString()};
                Inventaire selectedInventaire = dao.getInventaireFromHistory(params);
                System.out.println("Nomfr: " + selectedInventaire.getNomFr() + ", nomlatin: " + selectedInventaire.getNomLatin() + ", date: " + selectedInventaire.getDate() + ", heure: " + selectedInventaire.getHeure());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setAdapter(){
        List<Inventaire> inventaires = dao.getInventairesOfTheUsr(getCurrentUsrId());

        InventaireAdapter adapter = new InventaireAdapter(this,inventaires);
        listInv.setAdapter(adapter);
    }

    protected long getCurrentUsrId(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return loginPreferences.getLong("usrId",0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
