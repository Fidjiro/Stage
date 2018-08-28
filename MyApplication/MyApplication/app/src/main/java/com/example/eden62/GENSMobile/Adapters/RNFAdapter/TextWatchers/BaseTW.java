package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;

import java.util.List;

public abstract class BaseTW implements TextWatcher {

    public RNFInventaire item;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        textChangedAction(getInputFromEditable(s));
    }

    protected int getInputFromEditable(Editable s){
        int input = 0;

        try {
            input = Integer.parseInt(s.toString());
        }catch (Exception e ){
            // e.printStackTrace();
        }

        return input;
    }

    protected void textChangedAction(int input){
        setItemGoodField(input);
    }

    protected abstract void setItemGoodField(int input);
}
