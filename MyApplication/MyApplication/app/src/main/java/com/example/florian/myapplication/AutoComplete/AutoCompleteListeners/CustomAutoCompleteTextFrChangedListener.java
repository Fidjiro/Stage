package com.example.florian.myapplication.AutoComplete.AutoCompleteListeners;

import android.content.Context;

import com.example.florian.myapplication.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.florian.myapplication.Activities.MapsActivities.Recensement.SearchTaxonPopup;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomFrArrayAdapter;
import com.example.florian.myapplication.Database.LoadingDatabase.Taxon;
import com.example.florian.myapplication.R;

/**
 * Evènement qui affiche la liste de sugestion de noms français
 */
public class CustomAutoCompleteTextFrChangedListener extends CustomAutoCompleteTextChangedListener{

    public CustomAutoCompleteTextFrChangedListener(Context context) {
        super(context);
    }

    @Override
    protected void actionOnTextChanged(CharSequence userInput) {
        SearchTaxonPopup mainActivityRec = ((SearchTaxonPopup) context);

        // update the adapater
        mainActivityRec.myFrAdapter.notifyDataSetChanged();

        // get suggestions from the database
        Taxon[] myObjs = mainActivityRec.dao.readFr(userInput.toString());

        // update the adapter
        mainActivityRec.myFrAdapter = new AutocompleteCustomFrArrayAdapter(mainActivityRec, R.layout.list_view_row, myObjs);

        mainActivityRec.myAutoCompleteFr.setAdapter(mainActivityRec.myFrAdapter);
    }
}
