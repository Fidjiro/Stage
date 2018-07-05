package com.example.eden62.GENSMobile.Activities.Historiques;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.eden62.GENSMobile.HistoryAdapters.ItemsAdapter;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;
import java.util.List;

public abstract class HistoryActivity<T extends ItemsAdapter> extends AppCompatActivity {
    protected ListView listItems;
    protected CheckBox changeAllCheckboxes;
    protected T adapter;
    protected ViewGroup header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setView();

        setListViewHeader();
        listItems = (ListView) findViewById(R.id.listViewHistory);
        listItems.addHeaderView(header,null,false);

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                actionOnItemClick(adapterView, view, i, l);
            }
        });

        openDatabases();

        setAdapter();

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

        Button deleteSelection = (Button) findViewById(R.id.deleteSelect);
        deleteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });
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

    protected void deleteCheckedItems(){
        adapter.getCheckedItemsStocker().deleteCheckedItemsFromDao();
        setAdapter();
    }

    protected void changeAllCheckboxStatus(boolean checked){
        List<CheckBox> allCheckboxes = adapter.getAllCheckboxes();
        for(CheckBox cb : allCheckboxes){
            cb.setChecked(checked);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
        changeAllCheckboxes.setChecked(false);
    }

    protected void checkAll(){
        changeAllCheckboxStatus(true);
    }

    protected void uncheckAll(){
        changeAllCheckboxStatus(false);
    }


    protected abstract void setAdapter();

    protected abstract void setView();

    protected abstract void setListViewHeader();

    protected abstract void actionOnItemClick(AdapterView<?> adapterView, View view, int i, long l);

    protected abstract void openDatabases();
}
