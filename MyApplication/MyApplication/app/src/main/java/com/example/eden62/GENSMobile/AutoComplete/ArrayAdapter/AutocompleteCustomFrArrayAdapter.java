package com.example.eden62.GENSMobile.AutoComplete.ArrayAdapter;

import android.content.Context;
import android.graphics.Color;

import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.SearchTaxonPopup;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.Taxon;

/**
 * Liste adaptative des noms en français
 */
public class AutocompleteCustomFrArrayAdapter extends AutocompleteCustomArrayAdapter {
    public AutocompleteCustomFrArrayAdapter(Context mContext, int layoutResourceId, Taxon[] data) {
        super(mContext, layoutResourceId, data);
    }

    @Override
    protected String getNiceText(Taxon t) {
        return t.getNom_fr();
    }

    @Override
    protected int getNiceColor(Taxon t) {
        return Color.BLACK;
    }

    @Override
    protected void dismissDropDown() {
        SearchTaxonPopup activity = (SearchTaxonPopup) mContext;
        activity.myAutoCompleteFr.dismissDropDown();
    }
}
