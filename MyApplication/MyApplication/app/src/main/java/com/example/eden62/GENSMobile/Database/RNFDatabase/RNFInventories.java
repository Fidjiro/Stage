package com.example.eden62.GENSMobile.Database.RNFDatabase;

import java.util.ArrayList;

public class RNFInventories extends ArrayList<RNFInventaire> {

    public String noObservation(){
        return "Aucune observation";
    }

    public String nbEspecesObs(){
        int nbEspèces = getNbEspecesObs();
        return nbEspèces + getGoodStringFormat(nbEspèces);
    }

    public int getNbEspecesObs(){
        int res = 0;
        for(RNFInventaire inv : this){
            if(inv.getNombre() != 0 || inv.getNbFemale() != 0 || inv.getNbMale() != 0)
                res ++;
        }
        return res;
    }

    private String getGoodStringFormat(int i){
        if(i < 2)
            return " espèce observée";
        return " espèces observées";
    }
}
