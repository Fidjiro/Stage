package com.example.eden62.GENSMobile.Activities;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mapsforge.core.model.LatLong;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class RelToGpx {

    private String DIRECTORY_PATH;
    private static final String GPX_EXTENSION = ".gpx";
    private Context ctx;

    public RelToGpx(Context ctx, String packageName) {
        this.ctx = ctx;
        DIRECTORY_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + packageName + "/files/";
    }

    private String createGoodGpxSegment(Releve rel){
        String segments = "";
        if(rel.getType().equals("Point")) {
            segments = "<trkpt lat=\"" + rel.getLatitudes() + "\" lon=\"" + rel.getLongitudes() + "\"><time>" + formatDateHeure(rel) + "</time></trkpt>\n";
        }
        else{
            Gson gson = new Gson();
            Type type = new TypeToken<List<LatLong>>() {}.getType();
            String relLatLongsString = rel.getLat_long();
            List<LatLong> relLatLongs = gson.fromJson(relLatLongsString, type);

            for (LatLong location : relLatLongs) {
                segments += "<trkpt lat=\"" + location.getLatitude() + "\" lon=\"" + location.getLongitude() + "\"><time>" + formatDateHeure(rel) + "</time></trkpt>\n";
            }
        }
        return segments;
    }

    private void generateGpx(File file, Releve rel) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
        String name = "<name>" + rel.getNom() + "</name><trkseg>\n";
        String segments = createGoodGpxSegment(rel);
        String footer = "</trkseg></trk></gpx>";

        try {
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(header.getBytes());
            writer.write(name.getBytes());
            writer.write(segments.getBytes());
            writer.write(footer.getBytes());
            if(writer != null){
                writer.close();
            }

        } catch (IOException e) {
            Log.e("generateGpx", "Error Writting Path",e);
        }
    }

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

    public void export(Releve rel) {
        File dir = new File(DIRECTORY_PATH);
        if(!dir.exists())
            dir.mkdirs();
        String currFilePath = DIRECTORY_PATH + rel.getNom() + GPX_EXTENSION;
        File mFile = new File(currFilePath);
        try {
            if (appHasAccessToStorage()) {
                mFile.createNewFile();
                MediaScannerConnection.scanFile(ctx, new String[] {currFilePath}, null, null);
                generateGpx(mFile,rel);
            } else
                System.out.println("Pas accès");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean appHasAccessToStorage(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }
}
