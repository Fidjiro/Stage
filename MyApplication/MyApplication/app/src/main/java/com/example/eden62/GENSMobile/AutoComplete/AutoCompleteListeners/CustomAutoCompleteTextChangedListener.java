package com.example.eden62.GENSMobile.AutoComplete.AutoCompleteListeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Evènement déclenché lors de la modification du texte de l'autocompleteTextView qui permet d'afficher la liste de sugestion
 */
public abstract class CustomAutoCompleteTextChangedListener implements TextWatcher {

    Context context;
    public static final int THRESHOLD = 3;

    public CustomAutoCompleteTextChangedListener(Context context) {
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    /**
     * Ajoute l'évènement lancé lors de la modification de l'input
     *
     * @param userInput L'input de l'utilisateur
     * @param start inutilisé
     * @param before inutilisé
     * @param count inutilisé
     */
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        if(isValable(userInput)){
            try{
                actionOnTextChanged(userInput);
            }catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mets à jour la liste déroulante en fonction de l'input de l'utilisateur
     *
     * @param userInput L'input de l'utilisateur
     */
    protected abstract void actionOnTextChanged(CharSequence userInput);

    /**
     * Vérifie si la requête de recherche doit être lancé. La requête est lancé seulement si la longueur de l'usrInput
     * est supérieure ou égale au {@link CustomAutoCompleteTextChangedListener#THRESHOLD}
     *
     * @param userInput L'input de l'utilisateur
     * @return <code>True</code> si la requête doit être lancée, <code>false</code> sinon
     */
    protected boolean isValable(CharSequence userInput){
        return userInput.length() >= THRESHOLD;
    }
}
