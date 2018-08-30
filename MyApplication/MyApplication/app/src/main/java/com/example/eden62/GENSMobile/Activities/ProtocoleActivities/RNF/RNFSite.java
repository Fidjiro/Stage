package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.Site;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.Transect;

import java.util.ArrayList;

public class RNFSite extends Site {

    protected ArrayList<Transect> transects;

    public RNFSite(String name, ArrayList<Transect> transects) {
        super(name);
        this.transects = transects;
    }

    public ArrayList<Transect> getTransects() {
        return transects;
    }

    public void setTransects(ArrayList<Transect> transects) {
        this.transects = transects;
    }
}
