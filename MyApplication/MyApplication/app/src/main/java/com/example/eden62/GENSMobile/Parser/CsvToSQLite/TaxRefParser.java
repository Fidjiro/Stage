package com.example.eden62.GENSMobile.Parser.CsvToSQLite;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.Taxon;

/**
 * Parse le fichier Taxref dans les assets pour insérer son contenu dans la table équivalente
 */
public class TaxRefParser extends CSVParser {

    public TaxRefParser(Context context) {
        super(context, "referentiel.csv");
        this.nbColumns = 8;
    }

    @Override
    protected long insertCurrentLine(String[] columns, TaxUsrDAO dao) {
        int length = columns.length;
        long ref_taxon = Long.valueOf(columns[0].trim());
        String nom = columns[1].trim();
        String nomFr = columns[2].trim();
        int nv = Integer.valueOf(columns[3].trim());
        String regne = columns[4].trim();
        String classe = length < 6 ? "" : columns[5].trim() ;
        String ordre = length < 7 ? "" : columns[6].trim();
        String famille = length < 8 ? "" : columns[7].trim();

        Taxon tax = new Taxon(ref_taxon, nom, nomFr,nv, regne, classe, ordre, famille);

        return dao.insertTaxon(tax);
    }
}
