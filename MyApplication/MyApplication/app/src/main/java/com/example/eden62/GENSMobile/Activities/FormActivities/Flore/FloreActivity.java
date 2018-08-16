package com.example.eden62.GENSMobile.Activities.FormActivities.Flore;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

import com.example.eden62.GENSMobile.Activities.FormActivities.FormActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activité formulaire pour la flore
 */
public class FloreActivity extends FormActivity {

    protected Spinner indiceAbondance;
    protected static final List<IntegerTuple> INDICES = new ArrayList<>(Arrays.asList(new IntegerTuple(0,0),
            new IntegerTuple(1,25),
            new IntegerTuple(26,100),
            new IntegerTuple(101,1000),
            new IntegerTuple(1001,10000),
            new IntegerTuple(10001,IntegerTuple.INFINI)));

    protected int indiceAbondanceValue;

    /**
     * @see FormActivity#setView()
     */
    @Override
    protected void setView() {
        setContentView(R.layout.activity_form_flore);
    }

    /**
     * Extension de la méthode initFields mère pour adapter au layout flore
     */
    @Override
    protected void initFields() {
        super.initFields();
        indiceAbondance = (Spinner) findViewById(R.id.abondance);
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setIndiceFromDenombrement();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        typeTaxon = 1;
    }

    @Override
    protected void changeFieldsStates(boolean enabled) {
        super.changeFieldsStates(enabled);
        indiceAbondance.setEnabled(enabled);
    }

    @Override
    protected void setFieldsFromConsultedInv() {
        super.setFieldsFromConsultedInv();
        indiceAbondance.setSelection(consultedInv.getIndiceAbondance());
    }

    @Override
    protected void modifConsultedInventaire() {
        super.modifConsultedInventaire();
        setStockedValuesFromUsrInput();
        consultedInv.setIndiceAbondance(indiceAbondanceValue);
    }

    /**
     * Retourne un {@link Inventaire} pour une flaure avec l'attribut typeObs déjà à <bold>Vu</bold> car on est obligé de voir une fleur,
     * on ne peut pas l'entendre
     *
     * @return L'objet {@link Inventaire} à insérer dans la base
     */
    @Override
    protected Inventaire createPersonalInventaire() {
        return new Inventaire(ref_taxon, Utils.getVerCode(this), nv_taxon, usrId, nomFrString,nomLatinString, typeTaxon, lat, lon, dat, heure, nb, "Vu", remarquesTxt, indiceAbondanceValue);
    }

    @Override
    protected boolean formIsValidable() {
        return super.formIsValidable() && (isEmptyDenombrement() || coherentDenombrement());
    }

    /**
     * Vérifie que le dénombrement est cohérent avec l'indice d'abondance sélectionné
     *
     * @return <code>True</code> si le dénombrement est cohérant, <code>false</code> sinon
     */
    protected boolean coherentDenombrement(){
        int nombreText = getDenombrement();
        Integer indice = getSelectedIndiceValue();
        IntegerTuple bornes = INDICES.get(indice);

        return bornes.contains(nombreText);
    }

    /**
     * Récupère l'indice d'abondance sélectionné
     *
     * @return La string correspondant à l'indice d'abondance
     */
    protected String getSelectedIndice(){
        return indiceAbondance.getSelectedItem().toString();
    }

    /**
     * Récupère la valeur de l'indice d'abondance
     *
     * @return L'entier correspondant à l'indice d'abondance
     */
    protected int getSelectedIndiceValue(){
        Character c = getSelectedIndice().charAt(0);
        return Integer.parseInt(c.toString());
    }

    @Override
    protected void actionWhenFormNotValidable() {
        super.actionWhenFormNotValidable();
        if(!coherentDenombrement())
            nombre.setError(getString(R.string.incoherentDenombrement));
    }

    @Override
    protected void setStockedValuesFromUsrInput() {
        super.setStockedValuesFromUsrInput();

        indiceAbondanceValue = getSelectedIndiceValue();
    }

    /**
     * Représente les bornes des indices d'abondance. -1 est ici traduit comme l'infini
     */
    public static class IntegerTuple {
        private final int x;
        public final int y;
        public final static int INFINI = -1;

        public IntegerTuple(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Vérifie que l'entier en paramètre est contenu dans ses bornes x et y. Pour le dernier indice d'abondance, il fallait
         * quelque chose pour représenter l'infini vu que l'indice est plus de 10 000
         * @param i L'entier à comparer
         * @return <code>true</code> si i est compris dans les bornes, <code>false</code> sinon
         */
        public boolean contains(int i){
            if(y == INFINI)
                return x <= i;
            return (x <= i) && ( i <= y);
        }

        @Override
        public String toString() {
            return "Borne x : " + x + ", borne y : " + y;
        }
    }

    /**
     * Change l'indice d'abondance du formulaire en fonction du dénombrement inséré par l'utilisateur
     */
    protected void setIndiceFromDenombrement(){
        int denombrement = getDenombrement(), i = 0;

        while(!INDICES.get(i).contains(denombrement))
            i++;

        indiceAbondance.setSelection(i);
    }
}
