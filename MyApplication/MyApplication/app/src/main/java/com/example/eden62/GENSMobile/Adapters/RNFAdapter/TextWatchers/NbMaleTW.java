package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.widget.EditText;

import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;

import java.util.List;

public class NbMaleTW extends BaseGenreTW {

    public NbMaleTW(EditText nombre) {
        super(nombre);
    }

    @Override
    protected void setItemGoodField(int input) {
        item.setNbMale(input);
    }
}
