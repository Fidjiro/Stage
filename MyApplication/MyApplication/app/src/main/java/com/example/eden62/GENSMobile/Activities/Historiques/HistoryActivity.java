package com.example.eden62.GENSMobile.Activities.Historiques;

import android.app.Dialog;
import android.content.DialogInterface;
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

import com.example.eden62.GENSMobile.Adapters.HistoryAdapters.ItemsAdapter;
import com.example.eden62.GENSMobile.R;

import java.util.List;

/**
 * Activité d'historique de relevé ou inventaire
 *
 * @param <T> Adapter de relevés ou d'inventaires
 */
public abstract class HistoryActivity<T extends ItemsAdapter> extends AppCompatActivity {
    protected ListView listItems;
    protected CheckBox changeAllCheckboxes;
    protected T adapter;
    protected ViewGroup header;
    protected Button deleteSelection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setView();

        initFields();

        openDatabases();

        setAdapter();
    }

    /**
     * Initialise les champs de cette activitée
     */
    protected void initFields(){
        setListViewHeader();
        listItems = (ListView) findViewById(R.id.listViewHistory);
        listItems.addHeaderView(header,null,false);

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                actionOnItemClick(adapterView, view, i, l);
            }
        });

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

        deleteSelection = (Button) findViewById(R.id.deleteSelect);
        deleteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });
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

    /**
     * Supprime les items de la base puis réinitialise la listView
     */
    protected void deleteCheckedItems(){
        adapter.getCheckedItemsStocker().deleteCheckedItemsFromDao();
        setAdapter();
    }

    /**
     * Change le statut checked des checkboxes via le paramètre checked
     *
     * @param checked Le nouveau statut des checkboxes
     */
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabases();
    }

    /**
     * Coche toutes les checkboxes
     */
    protected void checkAll(){
        changeAllCheckboxStatus(true);
    }

    /**
     * Décoche toutes les checkboxes
     */
    protected void uncheckAll(){
        changeAllCheckboxStatus(false);
    }

    /**
     * Initialise l'adapter adéquat
     */
    protected abstract void setAdapter();

    /**
     * Utilise le bon layout pour la méthode setContentView()
     */
    protected abstract void setView();

    /**
     * Affecte à la listView un header personnalisé en fonction de la classe
     */
    protected abstract void setListViewHeader();

    /**
     * Action lancée lors d'un clic sur un item de la listView
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    protected abstract void actionOnItemClick(AdapterView<?> adapterView, View view, int i, long l);

    /**
     * Ouvre les BDD utile pour cette classe
     */
    protected abstract void openDatabases();

    /**
     * Ferme les BDD utile pour cette classe
     */
    protected abstract void closeDatabases();
}
