package com.example.eden62.GENSMobile.Activities.Historiques.Releves;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eden62.GENSMobile.R;

/**
 * Activité permettant de nommer le fichier gpx exporté
 */
public class NameExportFileActivity extends AppCompatActivity {

    protected LinearLayout nameFileLayout;
    protected CheckBox sendByMail;
    protected EditText fileName;
    protected TextView filePath;
    protected Button validName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_export_file);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        nameFileLayout = (LinearLayout) findViewById(R.id.nameExportFileLayout);
        sendByMail = (CheckBox) findViewById(R.id.senByMail);
        fileName = (EditText) findViewById(R.id.nomFile);
        filePath = (TextView) findViewById(R.id.filePath);
        validName = (Button) findViewById(R.id.validerNom);

        nameFileLayout.getLayoutParams().width = (int)(width*0.75);

        Intent intent = getIntent();
        filePath.setText("Sera stocké dans : " + intent.getStringExtra("filePath"));

        validName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fileName.getText().toString();
                if(!name.equals("")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("sendByMail",sendByMail.isChecked());
                    resultIntent.putExtra("gpxName",name);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }else{
                    createAvertissementDialog().show();
                }
            }
        });
    }

    /**
     * Créé un dialog d'avertissement lorsque l'utilisateur n'a pas rentré de nom de fichier
     *
     * @return Le dialog d'avertissement
     */
    protected Dialog createAvertissementDialog(){
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.nameExportFile));
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
