package com.example.eden62.GENSMobile.Activities.Historiques.Releves;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
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

import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpLigne;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpPoint;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.InfoRelevesPopups.PopUpPolygone;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.HistoryAdapters.ReleveAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryReleveActivity extends AppCompatActivity {

    protected ListView listReleves;
    protected HistoryDao dao;
    protected ReleveAdapter adapter;
    private CheckBox changeAllCheckboxes;
    private List<Releve> exportedReleves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new HistoryDao(this);
        dao.open();

        exportedReleves = new ArrayList<>();

        setContentView(R.layout.activity_history_releve);

        listReleves = (ListView) findViewById(R.id.listViewReleve);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.row_item_releves,listReleves,false);
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
        listReleves.addHeaderView(header,null,false);
        listReleves.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nomRelTxt = (TextView)view.findViewById(R.id.nomRel);
                TextView typeRelTxt = (TextView)view.findViewById(R.id.typeReleve);
                TextView heureRelTxt = (TextView)view.findViewById(R.id.heureReleve);

                Releve rel = dao.getReleveFromNomTypeHeure(new String[]{nomRelTxt.getText().toString(), typeRelTxt.getText().toString(), heureRelTxt.getText().toString()});

                Intent intent = generateGoodIntent(typeRelTxt.getText().toString());
                intent.putExtra("releve",rel);
                startActivity(intent);
            }
        });

        Button deleteSelection = (Button) findViewById(R.id.deleteSelect);
        Button exportSelection = (Button) findViewById(R.id.exportSelect);

        deleteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });

        exportSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(listReleves,"Exportation en cours",Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                adapter.getCheckedReleveStocker().exportReleves();
                snackbar.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryReleveActivity.this);
                builder.setMessage("stockageInterne/Android/data/" + HistoryReleveActivity.this.getPackageName() + "/files/");
                builder.setTitle("Localisation des fichiers");
                builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
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
    protected void onResume() {
        super.onResume();
        setAdapter();
        changeAllCheckboxes.setChecked(false);
    }

    private void setAdapter(){
        List<Releve> releves = dao.getReleveOfTheUsr(getCurrentUsrId());

        adapter = new ReleveAdapter(this,releves);
        listReleves.setAdapter(adapter);
    }

    /**
     * Récupère l'id de l'utilisateur actuel
     * @return l'id de l'utilisateur
     */
    protected long getCurrentUsrId(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return loginPreferences.getLong("usrId",0);
    }

    /**
     * Dialog de sûreté qui prévient l'utilisateur de la supression des items
     * @return Un dialog d'avertissement
     */
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    private void deleteCheckedItems(){
        //RemoveCheckedItemsFromAdapter doit être appelé avant deleteCheckedItemsFromDao car cette dernière delete les relevés
        //de la liste.
        exportedReleves.removeAll(adapter.getCheckedReleveStocker().deleteCheckedItemsFromDao());
        setAdapter();
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
}
