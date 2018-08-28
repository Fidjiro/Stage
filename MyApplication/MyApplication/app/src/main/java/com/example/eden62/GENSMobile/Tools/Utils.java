/*
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.eden62.GENSMobile.Tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

import static java.lang.Math.log;

/**
 * Méthodes utilitaire qui peuvent être utilisées dans l'application
 */
public final class Utils {

    public static final DecimalFormat dfLength = new DecimalFormat("0.##");
    public static final DecimalFormat dfPosWgs = new DecimalFormat("0.#####");
    public static final DecimalFormat dfPosL93 = new DecimalFormat("0.####");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Marker createMarker(Context c, int resourceIdentifier,
                                      LatLong latLong) {
        Drawable drawable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? c.getDrawable(resourceIdentifier) : c.getResources().getDrawable(resourceIdentifier);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        return new Marker(latLong, bitmap, 0, -bitmap.getHeight() / 2);
    }

    public static Paint createPaint(int color, int strokeWidth, Style style) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        return paint;
    }

    //Peut être utile plus tard
    //@SuppressWarnings("deprecation")
   /* @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static Marker createTappableMarker(final Context c, int resourceIdentifier,
                                       LatLong latLong) {
        Drawable drawable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? c.getDrawable(resourceIdentifier) : c.getResources().getDrawable(resourceIdentifier);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();
        return new Marker(latLong, bitmap, 0, -bitmap.getHeight() / 2) {
            @Override
            public boolean onTap(LatLong geoPoint, Point viewPosition,
                                 Point tapPoint) {
                if (contains(viewPosition, tapPoint)) {
                    Toast.makeText(c,
                            "The Marker was tapped " + geoPoint.toString(),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        };
    }*/

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File getFileFromAssets(Context ctx, String aFileName) throws IOException {
        File cacheFile = new File(ctx.getCacheDir(), aFileName);
        try {
            try (InputStream inputStream = ctx.getAssets().open(aFileName)) {
                try (FileOutputStream outputStream = new FileOutputStream(cacheFile)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Could not open "+aFileName, e);
        }
        return cacheFile;
    }

    /**
     * Récupère la date du jour du système
     * @return La date au format JJ/MM/AAAA
     */
    public static String getDate(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        // On réalise un +1 car Janvier est considéré comme 0
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        c.get(Calendar.HOUR_OF_DAY);

        return formatInt(day) + "/" + formatInt(month) + "/" + year;
    }

    /**
     * Renvoi la date en paramètre avec une année en 2 chiffres
     * @param date La date à convertir
     * @return La date convertie
     */
    public static String printDateWithYearIn2Digit(String date){
        int idxSlash = date.lastIndexOf("/");
        String year = date.substring(idxSlash + 1);
        String newYear = year.substring(year.length()-2);
        return date.replace(year,newYear);
    }

    /**
     * Récupère l'heure de l'appareil
     *
     * @return L'heure relevé au format hh:mm:ss
     */
    public static String getTime(){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int secondes = c.get(Calendar.SECOND);
        return formatInt(hours) + ":" + formatInt(minutes) + ":" + formatInt(secondes);
    }

    /**
     * Formate un chiffre en format c en format 0c (ex: 1 sera transformé en 01)
     * @param i L'entier à transformer
     * @return L'entier formaté
     */
    private static String formatInt(int i){
        if(i < 10)
            return "0" + i;
        return "" + i;
    }

    // Calcule l'arc tangente hyperbolique
    private static double atanh(double x){
        return (log(1+x) - log(1-x))/2;
    }

    /**
     * Transforme Une LatLong WGS en XY Lambert
     *
     * @param latLong La position à convertir
     * @return La position en Lambert
     */
    public static XY convertWgs84ToL93(LatLong latLong){

        double latitude = latLong.getLatitude();
        double longitude = latLong.getLongitude();

// définition des constantes
        double c= 11754255.426096; //constante de la projection
        double e= 0.0818191910428158; //première exentricité de l'ellipsoïde
        double n= 0.725607765053267; //exposant de la projection
        double xs= 700000; //coordonnées en projection du pole
        double ys= 12655612.049876; //coordonnées en projection du pole

// pré-calculs
        double lat_rad= latitude/180*Math.PI; //latitude en rad
        double lat_iso= atanh(Math.sin(lat_rad))-e*atanh(e*Math.sin(lat_rad)); //latitude isométrique

//calcul
        double x= ((c*Math.exp(-n*(lat_iso)))*Math.sin(n*(longitude-3)/180*Math.PI)+xs);
        double y= (ys-(c*Math.exp(-n*(lat_iso)))*Math.cos(n*(longitude-3)/180*Math.PI));
        return new XY(x,y);
    }

    private Utils() {
        throw new IllegalStateException();
    }

    /**
     * Ferme le clavier
     * @param ctx Le context de l'activité appellante
     * @param v La view possédant un clavier
     */
    public static void hideKeyboard(Context ctx, View v){
            InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    /**
     * Vérifie si l'utilisateur est connecté à internet
     */
    public static boolean isConnected(Context ctx) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Récupère l'id de l'utilisateur actuel
     * @return l'id de l'utilisateur
     */
    public static long getCurrUsrId(Context ctx){
        return ctx.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE).getLong("usrId",0);
    }

    /**
     * Récupère la version de l'application
     *
     * @param ctx Le context de l'activité appellante
     * @return La version de l'application installée
     */
    public static int getVerCode(Context ctx) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionCode;
    }
}
