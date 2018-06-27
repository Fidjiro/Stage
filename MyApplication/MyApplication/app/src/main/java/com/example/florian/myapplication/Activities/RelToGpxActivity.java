package com.example.florian.myapplication.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;
import com.example.florian.myapplication.Tools.XY;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mapsforge.core.model.LatLong;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RelToGpxActivity extends AppCompatActivity {

    private static final String PATH = Environment.getExternalStorageDirectory() + File.separator + "releve";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel_to_gpx);
        Intent intent = getIntent();
        Releve relToExport = intent.getParcelableExtra("relToExport");
        toto(relToExport);
    }

    public void generateGpx(File file, Releve rel) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
        String name = "<name>" + rel.getNom() + "</name><trkseg>\n";

        String segments = "";
        Gson gson = new Gson();
        Type type = new TypeToken<List<LatLong>>() {}.getType();
        String relLatLongsString = rel.getLat_long();
        List<LatLong> relLatLongs = gson.fromJson(relLatLongsString, type);

        //XY pos;

        for (LatLong location : relLatLongs) {
          //  pos = Utils.convertWgs84ToL93(location);
            segments += "<trkpt lat=\"" + location.getLatitude() + "\" lon=\"" + location.getLongitude() + "\"><time>" + formatDateHeure(rel) + "</time></trkpt>\n";
        }

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

        date.replace("/","-");
        StringBuilder lettersBuff = new StringBuilder(date);
        String date_inverse = lettersBuff.reverse().toString();

        return date_inverse + "T" + time + "Z";
    }

    private void toto(Releve rel) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/files/");
        if(!dir.exists())
            dir.mkdirs();
        File mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/files/" + rel.getNom() + ".gpx");
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
                mFile.createNewFile();
                generateGpx(mFile,rel);
            } else
                System.out.println("Pas accès");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
