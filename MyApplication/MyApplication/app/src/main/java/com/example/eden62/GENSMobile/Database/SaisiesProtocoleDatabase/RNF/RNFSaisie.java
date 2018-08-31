package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.ChooseTransectActivity;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.ProtocoleMeteo;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.Saisie;

import java.util.List;

/**
 * Objet représentant une saisie RNF
 */
public class RNFSaisie implements Saisie {

    protected List<Transect> transects;
    protected ProtocoleMeteo meteo;

    public RNFSaisie(List<Transect> transects, ProtocoleMeteo meteo) {
        this.transects = transects;
        this.meteo = meteo;
    }

    @Override
    public String getSynthese() {
        List<Transect> transects = getTransects();
        String res = "";
        for(Transect transect : transects){
            res += transect.toString() + "\n";
        } return res;
    }

    @Override
    public ProtocoleMeteo getMeteo(){
        return meteo;
    }

    @Override
    public void setMeteo(ProtocoleMeteo meteo) {
        this.meteo = meteo;
    }

    public List<Transect> getTransects(){
        return transects;
    }

    public void setTransects(List<Transect> transects) {
        this.transects = transects;
    }

    @Override
    public Class getFillingActivity() {
        return ChooseTransectActivity.class;
    }
}
