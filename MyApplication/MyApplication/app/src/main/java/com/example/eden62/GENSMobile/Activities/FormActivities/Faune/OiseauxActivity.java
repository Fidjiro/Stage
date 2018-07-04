package com.example.eden62.GENSMobile.Activities.FormActivities.Faune;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activité formulaire pour les oiseaux
 */
public class OiseauxActivity extends FauneActivity {

    protected Spinner activite,statut,nidification;
    protected LinearLayout nidificationLayout;
    protected final int POS_STATUT_NICHEUR = 3;
    protected final int POS_NIDIF_PROB = 2;
    protected final int POS_NIDIF_CERTAINE = 3;

    protected String activiteValue;
    protected String statutValue;
    protected String nidificationValue;

    /**
     * Extension de la méthode mère pour adapter au layout oiseaux
     */
    @Override
    protected void initFields() {
        super.initFields();
        activite = (Spinner) findViewById(R.id.activite);
        statut = (Spinner) findViewById(R.id.statut);
        nidification = (Spinner) findViewById(R.id.nidification);
        nidificationLayout = (LinearLayout) findViewById(R.id.nidificationLayout);

        typeTaxon = 2;

        activite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isAccouplementTextView(view) || isParadeTextView(view)) {
                    statut.setSelection(POS_STATUT_NICHEUR);
                    nidification.setSelection(POS_NIDIF_PROB);
                } else if(isPonteTextView(view) || isCouvaisonTextView(view)){
                    statut.setSelection(POS_STATUT_NICHEUR);
                    nidification.setSelection(POS_NIDIF_CERTAINE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        statut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(viewIsEqualsTo(view,"Nicheur"))
                    nidificationLayout.setVisibility(View.VISIBLE);
                else
                    hideNidificationField();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void changeFieldsStates(boolean enabled) {
        super.changeFieldsStates(enabled);
        activite.setEnabled(enabled);
        statut.setEnabled(enabled);
        nidification.setEnabled(enabled);
    }

    @Override
    protected void setFieldsFromConsultedInv() {
        super.setFieldsFromConsultedInv();
        setSpinnerSelection(activite,R.array.activités,consultedInv.getActivite());
        setSpinnerSelection(statut,R.array.statuts,consultedInv.getStatut());
        setSpinnerSelection(nidification,R.array.nidification,consultedInv.getNidif());
    }

    @Override
    protected void modifConsultedInventaire() {
        super.modifConsultedInventaire();

        consultedInv.setActivite(activiteValue);
        consultedInv.setStatut(statutValue);
        consultedInv.setNidif(nidificationValue);
    }

    private void setSpinnerSelection(Spinner spinner, int id, String object){
        String[] stringArray = getResources().getStringArray(id);
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList(stringArray));
        spinner.setSelection(stringList.indexOf(object));
    }

    /**
     * Permet de cacher le champ Nidification et de remettre à zéro sa valeur
     */
    protected void hideNidificationField(){
        int emptySelectionPos = 0;
        nidification.setSelection(emptySelectionPos);
        nidificationLayout.setVisibility(View.GONE);
    }

    @Override
    protected Inventaire createPersonalInventaire() {
        return new Inventaire(ref_taxon, nv_taxon, usrId, nomFrString, nomLatinString, typeTaxon, lat, lon, dat, heure, nb, obs, remarquesTxt, nbMale, nbFemale, activiteValue, statutValue, nidificationValue);
    }

    @Override
    protected void setValuesFromUsrInput() {
        super.setValuesFromUsrInput();

        activiteValue = (String) activite.getSelectedItem();
        statutValue = (String) statut.getSelectedItem();
        nidificationValue = (String) nidification.getSelectedItem();
    }

    /**
     * Vérifie si le texte de la view v est égal à la string s
     *
     * @param v La view à comparer
     * @param s La String à comparer
     * @return <code>True</code> si le texte de v est égal à la string s, <code>false</code> sinon
     */
    protected boolean viewIsEqualsTo(View v, String s){
        return ((TextView) v).getText().toString().equals(s);
    }

    /**
     * Vérifie si le texte de la view est égal à Accouplement
     *
     * @param v La view à comparer
     * @return <code>True</code> si le texte de v est égal à Accouplement, <code>false</code> sinon
     */
    protected boolean isAccouplementTextView(View v){
        return viewIsEqualsTo(v,"Accouplement");
    }

    /**
     * Vérifie si le texte de la view est égal à Parade
     *
     * @param v La view à comparer
     * @return <code>True</code> si le texte de v est égal à Parade, <code>false</code> sinon
     */
    protected boolean isParadeTextView(View v){
        return viewIsEqualsTo(v,"Parade");
    }

    /**
     * Vérifie si le texte de la view est égal à Ponte
     *
     * @param v La view à comparer
     * @return <code>True</code> si le texte de v est égal à Ponte, <code>false</code> sinon
     */
    protected boolean isPonteTextView(View v){
        return viewIsEqualsTo(v,"Ponte");
    }

    /**
     * Vérifie si le texte de la view est égal à Couvaison
     *
     * @param v La view à comparer
     * @return <code>True</code> si le texte de v est égal à Couvaison, <code>false</code> sinon
     */
    protected boolean isCouvaisonTextView(View v){
        return viewIsEqualsTo(v,"Couvaison");
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_form_oiseaux);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!statut.getSelectedItem().equals("Nicheur"))
            nidificationLayout.setVisibility(View.GONE);
    }

    @Override
    protected boolean formIsValidable() {
        return super.formIsValidable() && !missingNidificationField() ;
    }

    /**
     * Vérifie si l'utilisateur n'a pas oublié de renseigner la nidification s'il a choisi comme statut nicheur
     *
     * @return <code>True</code> si l'utilisateur à oublié de renseigner le champ nidification, <code>false</code> sinon
     */
    protected boolean missingNidificationField(){
        return statutIsNicheur() && isNotASelectedNidification();
    }

    /**
     * Vérifie que le statut choisi est Nicheur
     *
     * @return <code>True</code> si l'utilisateur à le statut nicheur, <code>false</code> sinon
     */
    protected boolean statutIsNicheur(){
        return statut.getSelectedItem().equals("Nicheur");
    }

    /**
     * Vérifie si l'utilisateur n'a pas renseigné la nidification
     *
     * @return <code>True</code> si l'utilisateur n'a pas de renseigné le champ nidification, <code>false</code> sinon
     */
    protected boolean isNotASelectedNidification(){
        String selectedItem = (String) nidification.getSelectedItem();
        return selectedItem.isEmpty();
    }

    @Override
    protected void actionWhenFormNotValidable() {
        super.actionWhenFormNotValidable();
        if(missingNidificationField()){
            TextView selectedView = (TextView) nidification.getSelectedView();
            selectedView.setError(getString(R.string.emptyNidif));
            Toast.makeText(this,getString(R.string.emptyNidif),Toast.LENGTH_LONG).show();
        }
    }
}
