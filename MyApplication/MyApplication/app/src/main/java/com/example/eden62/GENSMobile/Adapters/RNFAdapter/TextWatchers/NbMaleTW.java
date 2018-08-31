package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.widget.EditText;

/**
 * TextWatcher de l'editText nombre mâle
 */
public class NbMaleTW extends BaseGenreTW {

    public NbMaleTW(EditText nombre) {
        super(nombre);
    }

    @Override
    protected void setItemGoodField(int input) {
        item.setNbMale(input);
    }
}
