package com.example.florian.myapplication.AutoComplete.AutoCompleteListeners;

import android.content.Context;

import com.example.florian.myapplication.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.florian.myapplication.Activities.MapsActivities.Recensement.SearchTaxonPopup;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomLatinArrayAdapter;
import com.example.florian.myapplication.Database.LoadingDatabase.Taxon;
import com.example.florian.myapplication.R;

/**
 * Evènement qui affiche la liste de sugestion de noms latin
 */
public class CustomAutoCompleteTextLatinChangedListener extends CustomAutoCompleteTextChangedListener {

    public CustomAutoCompleteTextLatinChangedListener(Context context) {
        super(context);
    }

    @Override
    protected void actionOnTextChanged(CharSequence userInput) {

        SearchTaxonPopup mainActivityRec = ((SearchTaxonPopup) context);

        // update the adapater
        mainActivityRec.myLatinAdapter.notifyDataSetChanged();

        // get suggestions from the database
        Taxon[] myObjs = mainActivityRec.dao.readLatin(userInput.toString());

        // update the adapter
        mainActivityRec.myLatinAdapter = new AutocompleteCustomLatinArrayAdapter(mainActivityRec, R.layout.list_view_row, myObjs);

        mainActivityRec.myAutoCompleteLatin.setAdapter(mainActivityRec.myLatinAdapter);
    }
}
