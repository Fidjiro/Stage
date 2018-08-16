package com.example.eden62.GENSMobile.Database.RNFDatabase;

public class RNFInventaire {

    protected String nomLatin;
    protected String nomFr;
    protected int nombre;
    protected int nbMale;
    protected int nbFemale;

    public RNFInventaire(String nomLatin, String nomFr, int nombre, int nbMale, int nbFemale) {
        this.nomLatin = nomLatin;
        this.nomFr = nomFr;
        this.nombre = nombre;
        this.nbMale = nbMale;
        this.nbFemale = nbFemale;
    }

    public String getNomLatin() {
        return nomLatin;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public String getNomFr() {
        return nomFr;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public int getNbMale() {
        return nbMale;
    }

    public void setNbMale(int nbMale) {
        this.nbMale = nbMale;
    }

    public int getNbFemale() {
        return nbFemale;
    }

    public void setNbFemale(int nbFemale) {
        this.nbFemale = nbFemale;
    }
}
