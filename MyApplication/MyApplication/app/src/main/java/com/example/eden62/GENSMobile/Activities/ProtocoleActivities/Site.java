package com.example.eden62.GENSMobile.Activities.ProtocoleActivities;

public class Site{

    protected String name;

    public Site(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
