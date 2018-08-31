package com.example.eden62.GENSMobile.Tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * TextWatcher pour les champs d'heures permettant de passer à l'EditText next lors de la fin de saisie
 */
public class HeureMinutesWatcher implements TextWatcher {

    EditText next;
    protected static final int EDIT_TEXT_SIZE = 2;

    public HeureMinutesWatcher(EditText next) {
        this.next = next;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() == EDIT_TEXT_SIZE)
            next.requestFocus();
    }

    @Override
    public void afterTextChanged(Editable s) { }
}
