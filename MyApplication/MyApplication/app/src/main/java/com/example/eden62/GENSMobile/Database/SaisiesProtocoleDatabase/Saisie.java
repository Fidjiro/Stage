package com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase;

public interface Saisie {

    /**
     * Résumé de la saisie qui sera affiché lors du clic sur l'une d'entre elles dans l'historique
     *
     * @return La synthèse de cette saisie
     */
    String getSynthese();

    ProtocoleMeteo getMeteo();

    /**
     * Récupère l'activité qui permet de remplir cette saisie
     *
     * @return l'activité correspondante à cette saisie
     */
    Class getFillingActivity();

    /**
     * Affecte la météo de cette saisie
     *
     * @param meteo La météo à affecter
     */
    void setMeteo(ProtocoleMeteo meteo);
}
