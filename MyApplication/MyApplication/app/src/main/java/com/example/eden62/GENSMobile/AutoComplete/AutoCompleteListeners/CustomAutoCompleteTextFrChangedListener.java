package com.example.eden62.GENSMobile.AutoComplete.AutoCompleteListeners;

import android.content.Context;

import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.SearchTaxonPopup;
import com.example.eden62.GENSMobile.AutoComplete.ArrayAdapter.AutocompleteCustomFrArrayAdapter;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.Taxon;
import com.example.eden62.GENSMobile.R;

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
        Taxon[] myObjs = mainActivityRec.dao.readFr(userInput.toString().trim());

        // update the adapter
        mainActivityRec.myFrAdapter = new AutocompleteCustomFrArrayAdapter(mainActivityRec, R.layout.list_view_row, myObjs);

        mainActivityRec.myAutoCompleteFr.setAdapter(mainActivityRec.myFrAdapter);
    }
}
