package com.example.florian.myapplication.Activities.MapsActivities.Recensement;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
                System.out.println("toto");
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
