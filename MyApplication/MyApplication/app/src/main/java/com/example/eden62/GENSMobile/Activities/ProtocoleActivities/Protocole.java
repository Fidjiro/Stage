package com.example.eden62.GENSMobile.Activities.ProtocoleActivities;

import java.util.List;

/**
 * Objet représentant un protocole
 */
public class Protocole {

    private String name;
    private List<Site> availableSite;
    private Class activityToLaunch;

    public Protocole(String name, List<Site> availableSite, Class activityToLaunch) {
        this.name = name;
        this.availableSite = availableSite;
        this.activityToLaunch = activityToLaunch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Site> getAvailableSites() {
        return availableSite;
    }

    public void setAvailableSite(List<Site> availableSite) {
        this.availableSite = availableSite;
    }

    public Class getActivityToLaunch() {
        return activityToLaunch;
    }

    public void setActivityToLaunch(Class activityToLaunch) {
        this.activityToLaunch = activityToLaunch;
    }

    @Override
    public String toString() {
        return getName();
    }
}
