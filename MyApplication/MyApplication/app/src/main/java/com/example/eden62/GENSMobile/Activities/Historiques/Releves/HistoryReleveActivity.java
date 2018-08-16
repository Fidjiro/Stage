package com.example.eden62.GENSMobile.Activities.Historiques.Releves;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Activities.Historiques.HistoryActivity;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpLigne;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpPoint;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpPolygone;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.Adapters.HistoryAdapters.ReleveAdapter;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Historiques des rekevés réalisés
 */
public class HistoryReleveActivity extends HistoryActivity<ReleveAdapter> {

    protected HistoryDao dao;
    protected Map<Releve,File> exportedReleves;
    static final int CHOOSE_GPX_NAME = 1;

    @Override
    protected void initFields() {
        super.initFields();

        exportedReleves = new HashMap<>();
        Button exportSelection = (Button) findViewById(R.id.exportSelect);
        exportSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getCheckedItemsStocker().getCheckedItems().size() > 0) {
                    Intent intent = new Intent(HistoryReleveActivity.this, NameExportFileActivity.class);
                    intent.putExtra("filePath","stockageInterne/Android/data/" + HistoryReleveActivity.this.getPackageName() + "/files/");
                    startActivityForResult(intent,CHOOSE_GPX_NAME);
                } else{
                    Toast.makeText(HistoryReleveActivity.this,"Aucun relevés sélectionnés",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Snackbar exportSnackbar = Snackbar.make(listItems, "Exportation en cours", Snackbar.LENGTH_INDEFINITE);
        if(requestCode == CHOOSE_GPX_NAME){
            if(resultCode == RESULT_OK){
                String name = data.getStringExtra("gpxName");
                if(data.getBooleanExtra("sendByMail",false)){
                    exportSnackbar.show();
                    adapter.getCheckedItemsStocker().exportReleveAndSendMail(name);
                    exportSnackbar.dismiss();
                }else{
                    exportSnackbar.show();
                    adapter.getCheckedItemsStocker().exportReleves(name);
                    exportSnackbar.dismiss();
                }
            }
        }
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
    protected void closeDatabases() {
        dao.close();
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
        List<Releve> releves = dao.getReleveOfTheUsr(Utils.getCurrUsrId(HistoryReleveActivity.this));

        adapter = new ReleveAdapter(this,releves,exportedReleves);
        listItems.setAdapter(adapter);
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
