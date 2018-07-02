package com.example.eden62.GENSMobile.Parser.CsvToSQLite;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Permet de parser un fichier et l'insérer dans sa table correspondante
 */
public abstract class CSVParser {

    protected String fileName;
    protected Context ctx;
    protected int nbColumns;
    protected static final String SPLITER = "\";\"";

    public CSVParser(Context context, String filename){
        fileName = filename;
        ctx = context;
    }

    /**
     * Créé un buffer prêt à être utilisé sur le fichier nommé fileName
     *
     * @return Le buffer initialisé sur le fichier nommé fileName
     */
    protected BufferedReader getBufferOnFileName(){
        AssetManager manager = ctx.getAssets();
        InputStream inStream;
        try {
            inStream = manager.open(fileName);
            return new BufferedReader(new InputStreamReader(inStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insère les données du fichier dans la base TaxUsr
     *
     * @param dao La base de données dans lesquelles les données sont insérés
     */
    public void parseCSVFileToDb(TaxUsrDAO dao) {
        BufferedReader buffer = getBufferOnFileName();
        String line;
        SQLiteDatabase db = dao.getDb();
        db.beginTransaction();
        try {
            buffer.readLine();
            while ((line = buffer.readLine()) != null) {
                line = line.substring(1, line.length() - 1);
                String[] columns = line.split(SPLITER);
                insertCurrentLine(columns, dao);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            buffer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Insère les valeurs dans colums dans la base dao
     *
     * @param columns Les valeurs à insérer
     * @param dao La base où on insèrera les valeurs
     * @return Le résultat de l'insertion
     */
    protected abstract long insertCurrentLine(String[] columns, TaxUsrDAO dao);
}
