package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFInventaire;

/**
 * TextWatcher de base pour les editText de dénombrement
 */
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

    /**
     * Récupère un int via l'editable
     *
     * @param s L'editable qui contient l'int
     * @return L'int correspondant à l'editable
     */
    protected int getInputFromEditable(Editable s){
        int input = 0;

        try {
            input = Integer.parseInt(s.toString());
        }catch (Exception e ){
            // e.printStackTrace();
        }

        return input;
    }

    /**
     * Action spécifique pour chaque TextWatcher réalisée dans le afterTextChanged
     *
     * @param input L'input de l'utilisateur dans l'editText
     */
    protected void textChangedAction(int input){
        setItemGoodField(input);
    }

    /**
     * Affecte le champ de l'inventaire correspondant à l'editText
     *
     * @param input L'input de l'utilisateur
     */
    protected abstract void setItemGoodField(int input);
}
