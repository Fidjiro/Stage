package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF;

import java.util.ArrayList;
import java.util.List;

/**
 * Liste regroupant tous les inventaires recensés d'un transect
 */
public class RNFInventories extends ArrayList<RNFInventaire> {

    public RNFInventories(){
        super();
    }

    public RNFInventories(List<RNFInventaire> listToAdd){
        super(listToAdd);
    }

    public String noObservation(){
        return "Aucune observation";
    }

    public String nbEspecesObs(){
        int nbEspèces = getNbEspecesObs();
        return nbEspèces + getGoodStringFormat(nbEspèces);
    }

    /**
     * Récupère le nombre d'espèces recensées
     *
     * @return Le nombre d'espèces ayant été recensées
     */
    public int getNbEspecesObs(){
        return size();
    }

    // Mets un s ou non en fonction de i
    private String getGoodStringFormat(int i){
        if(i < 2)
            return " espèce observée";
        return " espèces observées";
    }

    /**
     * Vérifie que chaque dénombrement total de chaque erspèce est cohérent avec le total du genre
     *
     * @return <code>True</code> si tous les dénombrements sont cohérent, <code>false</code> sinon
     */
    public boolean allDenombrementAreCoherent(){
        for(RNFInventaire inv : this){
            if(!inv.hasCoherentDenombrement())
                return false;
        } return true;
    }

    public boolean allNeededDenombrementAreNoted(){
        for(RNFInventaire inv : this){
            if((inv.getNbGenre() > 0) && (inv.getNombre() == 0) )
                return false;
        } return true;
    }

    @Override
    public String toString() {
        if(getNbEspecesObs() == 0)
            return noObservation();
        return nbEspecesObs();
    }
}
