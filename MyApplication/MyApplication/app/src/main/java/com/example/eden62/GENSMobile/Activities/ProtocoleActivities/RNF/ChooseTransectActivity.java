package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TransectAdapter;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFMeteo;
import com.example.eden62.GENSMobile.Database.RNFDatabase.Transect;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseTransectActivity extends AppCompatActivity {

    protected ListView listTransect;
    protected RNFMeteo meteo;
    protected int launchedTransectPos;
    public static final int RESULT_TRANSECT_DONE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_transect);

        listTransect = (ListView) findViewById(R.id.listTransect);

        final List<Transect> transects = new ArrayList<>();
        transects.add(new Transect("Transect 1",100));
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
                startActivityForResult(null,RESULT_TRANSECT_DONE);
            }
        });

        meteo = getIntent().getParcelableExtra("meteo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_TRANSECT_DONE){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
