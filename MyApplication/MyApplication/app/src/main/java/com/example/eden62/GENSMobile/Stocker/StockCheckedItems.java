package com.example.eden62.GENSMobile.Stocker;

import com.example.eden62.GENSMobile.Database.DAO;
import com.example.eden62.GENSMobile.Database.DatabaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Objet qui permet de stocker les items sélectionnés via les historiques
 *
 * @param <T> Le type d'items stockés
 * @param <Y> Le type de base de données correspondant à T
 */
public abstract class StockCheckedItems<T extends DatabaseItem,Y extends DAO> {

    protected List<T> checkedItems;
    protected Y dao;

    public StockCheckedItems(List<T> checkedItems, Y dao) {
        this.checkedItems = checkedItems;
        this.dao = dao;
    }

    /**
     * Ajoute un item dans la liste de sélection
     * @param item L'item à ajouter
     */
    public void add(T item){
        checkedItems.add(item);
    }

    /**
     * Supprime un item dans la liste de sélection
     * @param item L'item à supprimer
     */
    public void remove(T item){
        checkedItems.remove(item);
    }

    /**
     * Supprime les items selectionnés de la base de données correspondante
     */
    public List<T> deleteCheckedItemsFromDao(){
        dao.open();
        List<T> deletedItems = new ArrayList<>();
        for(T item : checkedItems){
            deletedItems.add(item);
            dao.delete(item);
        }
        dao.close();
        checkedItems = new ArrayList<>();
        return deletedItems;
    }

    /**
     * Récupère les items selectionés
     * @return La liste d'items
     */
    public List<T> getCheckedItems() {
        return checkedItems;
    }
}
