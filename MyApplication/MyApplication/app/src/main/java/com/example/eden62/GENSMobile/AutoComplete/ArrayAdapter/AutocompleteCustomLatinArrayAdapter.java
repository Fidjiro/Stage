package com.example.eden62.GENSMobile.AutoComplete.ArrayAdapter;

import android.content.Context;
import android.graphics.Color;

import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.SearchTaxonPopup;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.Taxon;

/**
 * Liste adaptative des noms en latin
 */
public class AutocompleteCustomLatinArrayAdapter extends AutocompleteCustomArrayAdapter {
    public AutocompleteCustomLatinArrayAdapter(Context mContext, int layoutResourceId, Taxon[] data) {
        super(mContext, layoutResourceId, data);
    }

    @Override
    protected String getNiceText(Taxon t){
        String res = t.getNom();
        if(t.getNv() == 5)
            return res + " sp.";
        return res;
    }

    protected int getNiceColor(Taxon t){
        int textColor;
        int nv = t.getNv();
        if(nv == 5){
            textColor = Color.BLUE;
        } else if(nv == 6)
            textColor = Color.BLACK;
        else if(nv == 7)
            textColor = Color.GRAY;
        else
            textColor = Color.rgb(255,140,0);
        return textColor;
    }

    @Override
    protected void dismissDropDown() {
        SearchTaxonPopup activity = (SearchTaxonPopup) mContext;
        activity.myAutoCompleteLatin.dismissDropDown();
    }
}
