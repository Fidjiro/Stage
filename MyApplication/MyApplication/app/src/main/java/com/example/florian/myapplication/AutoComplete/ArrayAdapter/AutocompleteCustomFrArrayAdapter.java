package com.example.florian.myapplication.AutoComplete.ArrayAdapter;

import android.content.Context;
import android.graphics.Color;

import com.example.florian.myapplication.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.florian.myapplication.Database.LoadingDatabase.Taxon;

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
        MainActivityRec activity = (MainActivityRec) mContext;
        activity.myAutoCompleteFr.dismissDropDown();
    }
}
