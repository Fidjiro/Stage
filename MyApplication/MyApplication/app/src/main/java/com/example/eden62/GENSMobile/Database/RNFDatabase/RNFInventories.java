package com.example.eden62.GENSMobile.Database.RNFDatabase;

import java.util.ArrayList;

public class RNFInventories extends ArrayList<RNFInventaire> {
    
    public String toString(boolean nothingObserved) {
        if(nothingObserved)
            return "Aucune observation";
        int nbEspèces = size();
        return nbEspèces + getGoodStringFormat(nbEspèces);
    }

    private String getGoodStringFormat(int i){
        if(i < 2)
            return " espèce observée";
        return " espèces observées";
    }
}
