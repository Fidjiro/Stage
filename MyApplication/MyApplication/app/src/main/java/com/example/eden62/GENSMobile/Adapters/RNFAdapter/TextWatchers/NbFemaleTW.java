package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.widget.EditText;

import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;

import java.util.List;

public class NbFemaleTW extends BaseGenreTW {

    public NbFemaleTW(EditText nombre){
        super(nombre);
    }

    @Override
    protected void setItemGoodField(int input) {
        item.setNbFemale(input);
    }
}
