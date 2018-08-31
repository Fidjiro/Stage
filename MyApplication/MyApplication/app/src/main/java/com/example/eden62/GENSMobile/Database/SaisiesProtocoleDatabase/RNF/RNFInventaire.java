package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Objet représentant un inventaire pour le RNF (représentant un papillon)
 */
public class RNFInventaire implements Parcelable{

    protected long ref_taxon;
    protected String nomLatin;
    protected String nomFr;
    protected int nv;
    protected int nombre;
    protected int nbMale;
    protected int nbFemale;

    public RNFInventaire(long ref_taxon, String nomLatin, String nomFr, int nv){
        this.ref_taxon = ref_taxon;
        this.nomLatin = nomLatin;
        this.nomFr = nomFr;
        this.nv = nv;
    }

    public RNFInventaire(long ref_taxon, String nomLatin, String nomFr, int nv, int nombre, int nbMale, int nbFemale) {
        this(ref_taxon, nomLatin,nomFr, nv);
        this.nombre = nombre;
        this.nbMale = nbMale;
        this.nbFemale = nbFemale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nomLatin);
        parcel.writeString(nomFr);
        parcel.writeInt(nombre);
        parcel.writeInt(nbMale);
        parcel.writeInt(nbFemale);
    }

    public static final Parcelable.Creator<RNFInventaire> CREATOR = new Parcelable.Creator<RNFInventaire>() {
        public RNFInventaire createFromParcel(Parcel in) {
            return new RNFInventaire(in);
        }

        public RNFInventaire[] newArray(int size) {
            return new RNFInventaire[size];
        }
    };

    private RNFInventaire(Parcel in) {
        nomLatin = in.readString();
        nomFr = in.readString();
        nombre = in.readInt();
        nbMale = in.readInt();
        nbFemale = in.readInt();
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

    public int getNbGenre(){
        return nbMale + nbFemale;
    }

    /**
     * Verifie si le dénombrement est cohérent
     *
     * @return <code>True</code> si le dénombrement est cohérent, <code>false</code> sinon
     */
    public boolean hasCoherentDenombrement(){
        return nombre == 0 || (getNbGenre()) <= nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RNFInventaire that = (RNFInventaire) o;
        return nomLatin.equals(that.nomLatin) &&
                nomFr.equals(that.nomFr);
    }

    @Override
    public int hashCode() {
        return nomLatin.hashCode();
    }
}
