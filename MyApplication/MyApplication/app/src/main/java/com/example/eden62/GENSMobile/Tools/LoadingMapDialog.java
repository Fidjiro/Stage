package com.example.eden62.GENSMobile.Tools;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.eden62.GENSMobile.R;

/**
 * ProgressDialog qui sert pour afficher le message d'avertissement de chargement de la carte
 */
public class LoadingMapDialog extends ProgressDialog {

    public LoadingMapDialog(Context context) {
        super(context);
        setMessage(context.getString(R.string.chargement));
        setIndeterminate(true);
        setCancelable(false);
    }

    /**
     * Permet d'afficher ou de cacher le dialog
     *
     * @param show Si <code>true</code>, on affiche le dialog, on le cache sinon
     */
    public void show(boolean show){
        if (show)
            show();
        else
            dismiss();
    }
}
