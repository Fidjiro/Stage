package com.example.eden62.GENSMobile.AutoComplete.ArrayAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.SearchTaxonPopup;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.Taxon;
import com.example.eden62.GENSMobile.R;

/**
 * Classe permettant de fournir une liste adaptative de sugestion pour l'AutoCompleteTextView
 */
public abstract class AutocompleteCustomArrayAdapter extends ArrayAdapter<Taxon> {

    Context mContext;
    int layoutResourceId;
    Taxon data[];

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId, Taxon[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{

            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((SearchTaxonPopup) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            Taxon objectItem = data[position];
            String text = getNiceText(objectItem);
            int textColor = getNiceColor(objectItem);

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            textViewItem.setText(text);
            textViewItem.setTextColor(textColor);
            setOnClickListener(objectItem,textViewItem);
            // in case you want to add some style, you can do something like:
            textViewItem.setBackgroundColor(Color.LTGRAY);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    /**
     * Ajoute à la textView l'évènement qui remplira les champs nomLatin et nomFr avec le nomLatin et nomFr du taxon t
     *
     * @param t Le {@link Taxon} donnant ses noms
     * @param view La textView à laquelle ajouter l'évènement
     */
    protected void setOnClickListener(final Taxon t, TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchTaxonPopup activity = ((SearchTaxonPopup) mContext);
                activity.myAutoCompleteLatin.setText(t.getNom());
                activity.myAutoCompleteFr.setText(t.getNom_fr());
                dismissDropDown();
            }
        });
    }

    /**
     * Récupère le bon texte au bon format pour les textView qui représenteront les choix dans la liste déroulante
     *
     * @param t Le Taxon d'où on tire le nom
     * @return Le texte au bon format pour lest textView de la liste déroulante
     */
    protected abstract String getNiceText(Taxon t);

    /**
     * Récupère la bonne couleur de police pour les textView qui représenteront les choix dans la liste déroulante
     *
     * @param t Le Taxon d'où on tire le nom
     * @return Le texte avec la bonne couleur pour les textView de la liste déroulante
     */
    protected abstract int getNiceColor(Taxon t);

    /**
     * Fait disparaître le clavier correspondant à l'autoCompleteTextView
     */
    protected abstract void dismissDropDown();
}
