package com.example.eden62.GENSMobile.Parser.CsvToSQLite;

import android.content.Context;

import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.User;
import com.example.eden62.GENSMobile.Parser.CsvToSQLite.CSVParser;

/**
 * Parse le fichier User dans les assets pour insérer son contenu dans la table équivalente
 */
public class UserParser extends CSVParser {

    public UserParser(Context context) {
        super(context, "user.csv");
        this.nbColumns = 3;
    }

    @Override
    protected long insertCurrentLine(String[] columns, TaxUsrDAO dao) {
        long usrId = Long.valueOf(columns[0]);
        String login = columns[1].trim();

        User usr = new User(usrId,login);

        return dao.insertUsr(usr);
    }
}
