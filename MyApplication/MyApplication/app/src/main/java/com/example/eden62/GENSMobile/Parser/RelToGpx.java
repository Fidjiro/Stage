package com.example.eden62.GENSMobile.Parser;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mapsforge.core.model.LatLong;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Parseur de relevés en un fichier gpx
 */
public class RelToGpx implements Serializable{

    private String DIRECTORY_PATH;
    private static final String GPX_EXTENSION = ".gpx";
    private Context ctx;
    private static FileOutputStream writer;

    public RelToGpx(Context ctx, String packageName) {
        this.ctx = ctx;
        DIRECTORY_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + packageName + "/files/";
    }

    // Formate la date au bon format pour le gpx
    private String formatDateHeure(Releve rel){
        String date = new String (rel.getDate());
        String time = new String(rel.getHeure());
        String date_inverse = "";

        String[] splittedDate = date.split("/");
        for(int i = splittedDate.length - 1; i > 0; --i){
            date_inverse += splittedDate[i] + "-";
        }
        date_inverse += splittedDate[0];

        return date_inverse + "T" + time + "Z";
    }

    // Génère la partie segment du gpx file via le relevé
    private String createGoodGpxSegment(Releve rel){
        String segments = "";
        if(rel.getType().equals("Point")) {
            segments = "<wpt lat=\"" + rel.getLatitudes() + "\" lon=\"" + rel.getLongitudes() + "\"><name>" + rel.getNom() + "</name><time>" + formatDateHeure(rel) + "</time></wpt>\n";
        }
        else{
            // Récupération de la List<LatLong> via json
            Gson gson = new Gson();
            Type type = new TypeToken<List<LatLong>>() {}.getType();
            String relLatLongsString = rel.getLat_long();
            List<LatLong> relLatLongs = gson.fromJson(relLatLongsString, type);
            segments += "<trk>\n<name>" + rel.getNom() + "</name><trkseg>\n";

            for (LatLong location : relLatLongs) {
                segments += "<trkpt lat=\"" + location.getLatitude() + "\" lon=\"" + location.getLongitude() + "\"><time>" + formatDateHeure(rel) + "</time></trkpt>\n";
            }
            segments += "</trkseg></trk>\n";
        }
        return segments;
    }

    private void initGpxFile() throws IOException {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n";
        writer.write(header.getBytes());
    }

    private void addRelGpxToFile(Releve rel) throws IOException {
        String segments = createGoodGpxSegment(rel);
        writer.write(segments.getBytes());
    }

    private void endGpxFile() throws IOException {
        String footer = "</gpx>";
        writer.write(footer.getBytes());
    }

    //Ecrit dans le fichier file le gpx
    private void generateGpx(File file, List<Releve> rels) {

        try {
            writer = new FileOutputStream(file);
            initGpxFile();
            for (Releve rel : rels) {
                addRelGpxToFile(rel);
            }
            endGpxFile();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exporte les relevés rels dans un fichier nommé gpxName
     *
     * @param rels Les relevés à exporter dans un gpx
     * @param gpxName Le nom du fichier
     * @return Le fichier créé
     */
    public File exportReleves(List<Releve> rels, String gpxName){
        File dir = new File(DIRECTORY_PATH);
        if(!dir.exists())
            dir.mkdirs();
        String currFilePath = DIRECTORY_PATH + gpxName + GPX_EXTENSION;
        File mFile = new File(currFilePath);
        try {
            if(appHasAccessToStorage()) {
                mFile.createNewFile();
                // Scan le fichier pour qu'il soit visible quand l'appareil est connecté à l'ordinateur
                MediaScannerConnection.scanFile(ctx,new String[] {currFilePath},null, null);
                generateGpx(mFile,rels);
                return mFile;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    // Vérifie si l'application a accès au stockage du téléphone
    private boolean appHasAccessToStorage(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }
}
