package com.example.eden62.GENSMobile.Database;

/**
 * Permet de donner un type commun aux dao inventaires et relevés pour les fournir aux StockCheckedItems
 * @param <T> Le type d'items qu'il y a dans la dao
 */
public interface DAO<T extends DatabaseItem>{

    /**
     * Supprime la ligne correspondant à l'item dans la base
     *
     * @param item L'item a supprimer de la base
     * @return le resultat de la suppression de cet item
     */
    long delete(T item);

    /**
     * Ouvre la base de donénes que représente la dao
     */
    void open();

    /**
     * Ferme la base de donénes que représente la dao
     */
    void close();
}
