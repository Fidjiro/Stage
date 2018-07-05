package com.example.eden62.GENSMobile.Activities.Historiques.Releves;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

import com.example.eden62.GENSMobile.Activities.Historiques.HistoryActivity;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpLigne;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpPoint;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpPolygone;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.HistoryAdapters.ReleveAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryReleveActivity extends HistoryActivity<ReleveAdapter> {

    protected HistoryDao dao;
    protected Map<Releve,File> exportedReleves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button exportSelection = (Button) findViewById(R.id.exportSelect);

        exportedReleves = new HashMap<>();

        exportSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Snackbar exportSnackbar = Snackbar.make(listItems,"Exportation en cours",Snackbar.LENGTH_INDEFINITE);
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryReleveActivity.this);
                builder.setMessage("stockageInterne/Android/data/" + HistoryReleveActivity.this.getPackageName() + "/files/\n \nL'envoyer par mail ?");
                builder.setTitle("Localisation des fichiers");
                builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportSnackbar.show();
                        adapter.getCheckedItemsStocker().exportReleveAndSendMail();
                        exportSnackbar.dismiss();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        exportSnackbar.show();
                        adapter.getCheckedItemsStocker().exportReleves();
                        exportSnackbar.dismiss();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_history_releve);
    }

    @Override
    protected void openDatabases() {
        dao = new HistoryDao(this);
        dao.open();
    }

    @Override
    protected void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup)inflater.inflate(R.layout.row_item_releves,listItems,false);
    }

    /**
     * Lance le bon type de popUp d'informations en fonction du relevé
     * @param typeReleve Le type du relevé
     * @return L'intent du bon type de popUp
     */
    protected Intent generateGoodIntent(String typeReleve){
        if(typeReleve.equals(getString(R.string.point)))
            return new Intent(HistoryReleveActivity.this, PopUpPoint.class);
        if(typeReleve.equals(getString(R.string.line)))
            return new Intent(HistoryReleveActivity.this, PopUpLigne.class);
        return new Intent(HistoryReleveActivity.this, PopUpPolygone.class);
    }

    @Override
    protected void setAdapter() {
        List<Releve> releves = dao.getReleveOfTheUsr(getCurrentUsrId());

        adapter = new ReleveAdapter(this,releves,exportedReleves);
        listItems.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    @Override
    protected void actionOnItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView nomRelTxt = (TextView)view.findViewById(R.id.nomRel);
        TextView typeRelTxt = (TextView)view.findViewById(R.id.typeReleve);
        TextView heureRelTxt = (TextView)view.findViewById(R.id.heureReleve);

        Releve rel = dao.getReleveFromNomTypeHeure(new String[]{nomRelTxt.getText().toString(), typeRelTxt.getText().toString(), heureRelTxt.getText().toString()});

        Intent intent = generateGoodIntent(typeRelTxt.getText().toString());
        intent.putExtra("releve",rel);
        startActivity(intent);
    }
}
