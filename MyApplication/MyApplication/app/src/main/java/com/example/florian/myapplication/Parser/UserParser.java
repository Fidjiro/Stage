package com.example.florian.myapplication.Parser;

import android.content.Context;

import com.example.florian.myapplication.Database.LoadingDatabase.TaxUsrDAO;
import com.example.florian.myapplication.Database.LoadingDatabase.User;

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
        String mdp = columns[2].trim();

        User usr = new User(usrId,login,mdp);

        return dao.insertUsr(usr);
    }
}
