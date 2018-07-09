package com.example.eden62.GENSMobile.Activities.Historiques.Stocker;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;

import java.util.ArrayList;
import java.util.ListIterator;

public class InventoryStocker extends StockCheckedItems<Inventaire,CampagneDAO> {

    public InventoryStocker(Context context) {
        super(new ArrayList<Inventaire>(), new CampagneDAO(context));
    }

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

    private String getInvDay(Inventaire inv){
        return inv.getDate().substring(0,2);
    }
}
