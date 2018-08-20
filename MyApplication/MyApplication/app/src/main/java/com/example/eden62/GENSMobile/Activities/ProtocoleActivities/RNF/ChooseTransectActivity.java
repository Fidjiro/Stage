package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TransectAdapter;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventories;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFMeteo;
import com.example.eden62.GENSMobile.Database.RNFDatabase.Transect;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseTransectActivity extends AppCompatActivity {

    protected ListView listTransect;
    protected RNFMeteo meteo;
    protected int launchedTransectPos;
    protected List<Transect> transects;

    public static RNFInventories transect1Inventories;

    public static final int RESULT_TRANSECT_DONE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_transect);

        listTransect = (ListView) findViewById(R.id.listTransect);

        transects = new ArrayList<>();
        transects.add(new Transect("Transect 1",100));
        transect1Inventories = transects.get(0).getInventories();
        transect1Inventories.add(new RNFInventaire("Aglais io", "Paon-du-jour"));
        transect1Inventories.add(new RNFInventaire("Coenonympha pamphilus", "Fadet commun"));
        transect1Inventories.add(new RNFInventaire("Colias hyale", "Soufré"));
        transect1Inventories.add(new RNFInventaire("Pieris sp.", ""));
        transects.add(new Transect("Transect 2",103));
        transects.add(new Transect("Transect 3",102));
        transects.add(new Transect("Transect 4",103));
        transects.add(new Transect("Transect 5",100));

        listTransect.setAdapter(new TransectAdapter(this,transects));

        listTransect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchedTransectPos = position;
                Intent intent = new Intent(ChooseTransectActivity.this, FillTransectActivity.class);
                intent.putExtra("transect",transects.get(position));
                startActivityForResult(intent,RESULT_TRANSECT_DONE);
            }
        });

        meteo = getIntent().getParcelableExtra("meteo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_TRANSECT_DONE){
            Transect modifiedTransect = transects.get(launchedTransectPos);
            RNFInventories invs = modifiedTransect.getInventories();
            int nbEspeceObs = invs.getNbEspecesObs();

            if(nbEspeceObs == 0)
                modifiedTransect.setInfo(invs.noObservation());
            else
                modifiedTransect.setInfo(invs.nbEspecesObs());

            modifiedTransect.setDone(true);

            listTransect.setAdapter(new TransectAdapter(this,transects));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean allTransectsDone(){
        for(Transect t : transects){
            if(!t.isDone())
                return false;
        }
        return true;
    }
}
