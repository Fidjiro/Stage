package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.widget.EditText;

public class NbFemaleTW extends BaseGenreTW {

    public NbFemaleTW(EditText nombre){
        super(nombre);
    }

    @Override
    protected void setItemGoodField(int input) {
        item.setNbFemale(input);
    }
}
