package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import android.widget.EditText;

import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFInventaire;

/**
 * TextWatcher d'editText correspondante à un nombre de mâle ou femelle
 */
public abstract class BaseGenreTW extends BaseTW {

    protected int oldNbGenre;
    protected EditText nombre;

    public BaseGenreTW(EditText nombre){
        this.nombre = nombre;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        oldNbGenre = item.getNbGenre();
    }

    @Override
    protected void textChangedAction(int input) {
        super.textChangedAction(input);
        if(!isEmptyET(nombre))
            updateDenombrementETViaNbGenre(oldNbGenre, item, nombre);
    }

    // Vérifie si l'editText est vide ou non
    private boolean isEmptyET(EditText et){
        return et.getText().toString().isEmpty();
    }

    /**
     * Met à jour le contenu de l'édit text dénombrement en fonction du total du genre. Si inférieur, le dénombrement est ramené au total
     * du nombre de genre. Si égal, le dénombrement suit les changement des dénombreemnt de genre
     *
     * @param oldNbGenre Ancien total du nombre de genre avant modification par l'utilisateur
     */
    protected void updateDenombrementETViaNbGenre(int oldNbGenre, RNFInventaire inv, EditText nombre) {
        int nbGenre = inv.getNbGenre();
        int nb = inv.getNombre();
        boolean isInchoherantNb = nb < nbGenre;

        if(oldNbGenre > nbGenre && oldNbGenre == nb)
            nb -= (oldNbGenre - nbGenre);
        else {
            if (isInchoherantNb)
                nb = adjustNombre(inv);
        }
        String newString = "";
        if(nb > 0)
            newString += nb;
        nombre.setText(newString);
    }

    // Ajuste le dénombrement de l'inventaire s'il est inférieur au nombre total de genre
    private int adjustNombre(RNFInventaire inv){
        int nbGenre = inv.getNbGenre();
        int nb = inv.getNombre();
        if(nb < nbGenre)
            nb += nbGenre - nb;
        inv.setNombre(nb);
        return nb;
    }
}
