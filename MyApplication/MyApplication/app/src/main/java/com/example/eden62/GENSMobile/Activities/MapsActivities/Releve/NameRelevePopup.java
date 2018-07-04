package com.example.eden62.GENSMobile.Activities.MapsActivities.Releve;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.Releve;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;
import com.example.eden62.GENSMobile.Tools.XY;

import org.mapsforge.core.model.LatLong;

import static java.lang.Math.log;

public class NameRelevePopup extends AppCompatActivity {

    protected TextView lineLengthText,perimeterText, polygonAreaText, positionWGSText,positionL93Text;
    protected Button validNom;
    protected EditText nomReleve;
    protected LinearLayout loginLayout,positionWGSLayout,positionL93Layout;

    protected Releve releveToAdd;

    protected HistoryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_releve);

        dao = new HistoryDao(this);
        dao.open();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        nomReleve = (EditText) findViewById(R.id.nomRel);
        validNom = (Button) findViewById(R.id.validerNom);
        lineLengthText = (TextView) findViewById(R.id.lineLength);
        perimeterText = (TextView) findViewById(R.id.perimeter);
        polygonAreaText = (TextView) findViewById(R.id.polygonArea);
        positionWGSLayout = (LinearLayout) findViewById(R.id.positionWGSLayout);
        positionL93Layout = (LinearLayout) findViewById(R.id.positionL93Layout);
        positionWGSText = (TextView) findViewById(R.id.positionWGS);
        positionL93Text = (TextView) findViewById(R.id.positionL93);
        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);

        loginLayout.getLayoutParams().width = (int)(width*0.75);

        Intent intent = getIntent();
        releveToAdd = intent.getParcelableExtra("releveToAdd");

        displayGoodReleveInfos();

        validNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nomReleve.getText().toString().isEmpty()) {
                    releveToAdd.setNom(nomReleve.getText().toString());
                    dao.insertInventaire(releveToAdd);
                    Utils.hideKeyboard(getApplicationContext(),getCurrentFocus());
                    NameRelevePopup.this.finish();
                }else
                    createAvertissementDialog().show();
            }
        });
    }

    private void displayGoodReleveInfos() {
        String type = releveToAdd.getType();
        if(type.equals(getString(R.string.point))) {
            double lat = Double.parseDouble(releveToAdd.getLatitudes());
            double lon = Double.parseDouble(releveToAdd.getLongitudes());
            XY xy = Utils.convertWgs84ToL93(new LatLong(lat, lon));

            positionL93Layout.setVisibility(View.VISIBLE);
            positionWGSLayout.setVisibility(View.VISIBLE);
            positionWGSText.setText(Utils.dfPosWgs.format(lat) + " ; " + Utils.dfPosWgs.format(lon));
            positionL93Text.setText(Utils.dfPosL93.format(xy.x) + " ; " + Utils.dfPosL93.format(xy.y));
        } else if(type.equals(getString(R.string.line))){
            lineLengthText.setVisibility(View.VISIBLE);
            System.out.println(releveToAdd.getLength());
            lineLengthText.setText(getString(R.string.longueur) + " " + Utils.dfLength.format(releveToAdd.getLength()));
        } else{
            polygonAreaText.setVisibility(View.VISIBLE);
            perimeterText.setVisibility(View.VISIBLE);
            perimeterText.setText(getString(R.string.perimetre) + " " + Utils.dfLength.format(releveToAdd.getPerimeter()));
            polygonAreaText.setText(getString(R.string.polygonArea) + " " + Utils.dfLength.format(releveToAdd.getArea()));
        }
    }

    protected Dialog createAvertissementDialog(){
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.nommezReleve));
        builder.setTitle(getString(R.string.avertissement));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        box = builder.create();
        return box;
    }
}
