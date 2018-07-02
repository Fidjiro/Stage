package com.example.eden62.GENSMobile.Database.LoadingDatabase;

/**
 * Représente une ligne de la table Taxref
 */
public class Taxon {

    private long _id;
    private String nom;
    private String nom_fr;
    private int nv;
    private String regne;
    private String classe;
    private String ordre;
    private String famille;

    /**
     * Utilisé pour la génération de la liste de sugestion d'espèces
     *
     * @param _id
     * @param nom
     * @param nom_fr
     * @param nv
     */
    public Taxon(long _id, String nom, String nom_fr, int nv){
        this._id = _id;
        this.nom = nom;
        this.nom_fr = nom_fr;
        this.nv = nv;
    }

    /**
     * Permet d'insérer les taxons dans la base
     *
     * @param _id
     * @param nom
     * @param nom_fr
     * @param nv
     * @param regne
     * @param classe
     * @param ordre
     * @param famille
     */
    public Taxon(long _id, String nom, String nom_fr, int nv, String regne, String classe, String ordre, String famille) {
        this(_id,nom,nom_fr,nv);
        this.regne = regne;
        this.classe = classe;
        this.ordre = ordre;
        this.famille = famille;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom_fr() {
        return nom_fr;
    }

    public void setNom_fr(String nom_fr) {
        this.nom_fr = nom_fr;
    }

    public int getNv() {
        return nv;
    }

    public void setNv(int nv) {
        this.nv = nv;
    }

    public String getRegne() {
        return regne;
    }

    public void setRegne(String regne) {
        this.regne = regne;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }
}
