package com.example.florian.myapplication.Activities.FormActivities.Flore;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Spinner;

import com.example.florian.myapplication.Activities.FormActivities.FormActivity;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Activité formulaire pour la flore
 */
public class FloreActivity extends FormActivity {

    protected Spinner indiceAbondance;
    protected Map<Integer,IntegerTuple> indices;

    protected int indiceAbondanceValue;

    /**
     * @see FormActivity#setView()
     */
    @Override
    protected void setView() {
        setContentView(R.layout.activity_form_flore);
    }

    @Override
    protected void changeFieldsStates(boolean enabled) {
        super.changeFieldsStates(enabled);
        indiceAbondance.setEnabled(enabled);
    }

    /**
     * Retourne un {@link Inventaire} pour une flaure avec l'attribut typeObs déjà à <bold>Vu</bold> car on est obligé de voir une fleur,
     * on ne peut pas l'entendre
     * @return L'objet {@link Inventaire} à insérer dans la base
     */
    @Override
    protected Inventaire createPersonalInventaire() {
        return new Inventaire(ref_taxon, usrId, nomFrString,nomLatinString, typeTaxon, lat, lon, dat, heure, nb, "Vu", indiceAbondanceValue);
    }

    /**
     * Extension de la méthode initFields mère pour adapter au layout flore
     */
    @Override
    protected void initFields() {
        super.initFields();
        indiceAbondance = (Spinner) findViewById(R.id.abondance);
        nombre.setOnFocusChangeListener(new MyFloreFocusChangeListener());
        nombre.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    setIndiceFromDenombrement();
                }
                return false;
            }
        });

        typeTaxon = 1;
        setIndices();
    }

    @Override
    protected boolean formIsValidable() {
        return super.formIsValidable() && (isEmptyDenombrement() || coherentDenombrement());
    }

    /**
     * Vérifie si le champ dénombrement est vide
     */
    protected boolean isEmptyDenombrement(){
        return nombre.getText().toString().equals("");
    }

    /**
     * Vérifie que le dénombrement est cohérent avec l'indice d'abondance sélectionné
     *
     * @return <code>True</code> si le dénombrement est cohérant, <code>false</code> sinon
     */
    protected boolean coherentDenombrement(){
        int nombreText = getDenombrement();
        Integer indice = getSelectedIndiceValue();
        IntegerTuple bornes = indices.get(indice);

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

    /**
     * Affecte les bornes qui serviront à vérifier la bonne valeur du dénombrement en fonction de l'indice
     */
    protected void setIndices(){
        indices = new HashMap<>();
        indices.put(0,new IntegerTuple(0,0));
        indices.put(1,new IntegerTuple(1,25));
        indices.put(2,new IntegerTuple(26,100));
        indices.put(3,new IntegerTuple(101,1000));
        indices.put(4,new IntegerTuple(1001,10000));
        indices.put(5,new IntegerTuple(10001,IntegerTuple.INFINI));
    }

    @Override
    protected void actionWhenFormNotValidable() {
        super.actionWhenFormNotValidable();
        if(!coherentDenombrement())
            nombre.setError(getString(R.string.incoherentDenombrement));
    }

    @Override
    protected void setValuesFromUsrInput() {
        super.setValuesFromUsrInput();

        indiceAbondanceValue = getSelectedIndiceValue();
    }

    /**
     * Représente les bornes des indices d'abondance. -1 est ici traduit comme l'infini
     */
    public class IntegerTuple {
        public final int x;
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
            return (x <= i) && (y >= i);
        }

        @Override
        public String toString() {
            return "Borne x : " + x + ", borne y : " + y;
        }
    }

    protected void setIndiceFromDenombrement(){
        int denombrement;
        try{
            denombrement = getDenombrement();
        } catch(Exception e){
            denombrement = 0;
        }
        for (HashMap.Entry<Integer, IntegerTuple> entry : indices.entrySet()) {
            Integer ind = entry.getKey();
            IntegerTuple bornes = entry.getValue();
            if (bornes.contains(denombrement)) {
                indiceAbondance.setSelection(ind);
                return;
            }
        }
    }

    /**
     * Action réalisé quand le champ dénombrement perd le focus
     */
    public class MyFloreFocusChangeListener extends MyFocusChangeListener{

        /**
         * Permet de selectionner le bon indice d'abondance automatiquement via le dénombrement
         */
        @Override
        public void onFocusChange(View view, boolean b) {
            super.onFocusChange(view, b);
            if(!b) {
                setIndiceFromDenombrement();
            }
        }
    }
}
