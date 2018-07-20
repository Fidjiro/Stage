package com.example.eden62.GENSMobile.Stocker;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Stock les inventaires sélectionnés
 */
public class InventoryStocker extends StockCheckedItems<Inventaire,CampagneDAO> {

    public InventoryStocker(Context context) {
        super(new ArrayList<Inventaire>(), new CampagneDAO(context));
    }

    /**
     * Vérifie si parmis les inventaires stockés, il y en a qui ont des jours d'exécution différent
     *
     * @return <code>True</code> s'il y a deux jours d'exécution différents, <code>false</code> sinon
     */
    public boolean isTwoDifferentsDay(){
        String prevDay = "";
        String currDay;
        Inventaire currInv;
        ListIterator<Inventaire> it = checkedItems.listIterator();
        if(it.hasNext()){
            currInv = it.next();
            prevDay = getInvDay(currInv);
        }

        while (it.hasNext()){
            currInv = it.next();
            currDay = getInvDay(currInv);
            if(!prevDay.equals(currDay))
                return true;
            prevDay = currDay;
        }
        return false;
    }

    // Récupère le jour d'exécution d'un inventaire
    private String getInvDay(Inventaire inv){
        return inv.getDate().substring(0,2);
    }
}
