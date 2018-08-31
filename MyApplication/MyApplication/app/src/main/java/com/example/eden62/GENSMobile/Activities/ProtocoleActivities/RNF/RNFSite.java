package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.Site;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.Transect;

import java.util.ArrayList;

/**
 * Objet représentant un site correspondant au protocole RNF
 */
public class RNFSite extends Site {

    protected ArrayList<Transect> transects;

    public RNFSite(String name, ArrayList<Transect> transects) {
        super(name);
        this.transects = transects;
    }

    /**
     * Récupère les transects de ce site
     *
     * @return Les transects de ce site
     */
    public ArrayList<Transect> getTransects() {
        return transects;
    }

    /**
     * Affecte à ce site les transects en paramètres
     *
     * @param transects Les nouveaux transects de ce site
     */
    public void setTransects(ArrayList<Transect> transects) {
        this.transects = transects;
    }
}
