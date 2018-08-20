package com.example.eden62.GENSMobile.Adapters.RNFAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;

public class RNFInventaireAdapter extends ArrayAdapter<RNFInventaire>{

    //La liste complète
    private ArrayList<RNFInventaire> original;
    //La liste contenant les inventaires filtrés
    private ArrayList<RNFInventaire> fitems;
    private ArrayList<RNFInventaire> items;
    private Filter filterLat;
    private Filter filterFr;

    public RNFInventaireAdapter(Context context, @NonNull ArrayList<RNFInventaire> invs) {
        super(context, 0, invs);
        items = invs;
        original = new ArrayList<>(invs);
        fitems = new ArrayList<>(invs);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_rnf_inventaires,parent,false);

        RNFInventaireViewHolder viewHolder = (RNFInventaireViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RNFInventaireViewHolder();
            viewHolder.nomEspece = (TextView) convertView.findViewById(R.id.nomEspeceRNFInv);
            viewHolder.nombre = (EditText) convertView.findViewById(R.id.nombre);
            viewHolder.nbMale = (EditText) convertView.findViewById(R.id.nbMale);
            viewHolder.nbFemale = (EditText) convertView.findViewById(R.id.nbFemale);
            viewHolder.decNombreButton = (Button) convertView.findViewById(R.id.decDenombrement);
            viewHolder.incNombreButton = (Button) convertView.findViewById(R.id.incDenombrement);
            viewHolder.decNbMaleButton = (Button) convertView.findViewById(R.id.decNbMale);
            viewHolder.incNbMaleButton = (Button) convertView.findViewById(R.id.incNbMale);
            viewHolder.decNbFemaleButton = (Button) convertView.findViewById(R.id.decNbFemale);
            viewHolder.incNbFemaleButton = (Button) convertView.findViewById(R.id.incNbFemale);
        }

        final RNFInventaire inv = fitems.get(position);
        final RNFInventaire invTest = getItem(position);
        if(inv != null){
            String nom;
            String nomFr= inv.getNomFr();
            //il ne reste plus qu'à remplir notre vue
            nom = inv.getNomLatin();

            if(!nomFr.isEmpty())
                nom += " - " + nomFr;

            viewHolder.nomEspece.setText(nom);

            viewHolder.nombre.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int input = 0;
                    try {
                        input = Integer.parseInt(s.toString());
                    }catch (Exception e){
                        // e.printStackTrace();
                    }
                    invTest.setNombre(input);
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

            final RNFInventaireViewHolder finalViewHolder = viewHolder;
            viewHolder.nbMale.addTextChangedListener(new TextWatcher() {
                private int oldNbGenre;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { oldNbGenre = invTest.getNbGenre(); }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int input = 0;
                    try {
                        input = Integer.parseInt(s.toString());
                    }catch (Exception e ){
                        // e.printStackTrace();
                    }
                    invTest.setNbMale(input);
                    if(!isEmptyET(finalViewHolder.nombre))
                        updateDenombrementETViaNbGenre(oldNbGenre, invTest, finalViewHolder.nombre);
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

            viewHolder.nbFemale.addTextChangedListener(new TextWatcher() {
                private int oldNbGenre;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        oldNbGenre = invTest.getNbGenre();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int input = 0;
                    try {
                        input = Integer.parseInt(s.toString());
                    }catch (Exception e) {
                        //  e.printStackTrace();
                    }
                    invTest.setNbFemale(input);
                    if(!isEmptyET(finalViewHolder.nombre))
                        updateDenombrementETViaNbGenre(oldNbGenre, invTest, finalViewHolder.nombre);
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });


            viewHolder.decNombreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decNombre(invTest, finalViewHolder.nombre);
                }
            });

            viewHolder.incNombreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    incNombre(invTest, finalViewHolder.nombre);
                }
            });


            viewHolder.decNbMaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decNbMale(invTest, finalViewHolder.nbMale);
                }
            });

            viewHolder.incNbMaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    incNbMale(invTest, finalViewHolder.nbMale);
                }
            });

            viewHolder.decNbFemaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decNbFemale(invTest,finalViewHolder.nbFemale);
                }
            });

            viewHolder.incNbFemaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    incNbFemale(invTest,finalViewHolder.nbFemale);
                }
            });

            int nb = inv.getNombre();
            int nbMale = inv.getNbMale();
            int nbFemale = inv.getNbFemale();
            // Si le dénombrement n'a pas été défini
            if(nb != 0)
                viewHolder.nombre.setText(nb + "");
            else
                viewHolder.nombre.setText("");

            if(nbMale != 0)
                viewHolder.nbMale.setText(nbMale + "");
            else
                viewHolder.nbMale.setText("");

            if(nbFemale != 0)
                viewHolder.nbFemale.setText(nbFemale + "");
            else
                viewHolder.nbFemale.setText("");
        }

        return convertView;
    }

    private boolean isEmptyET(EditText et){
        return et.getText().toString().isEmpty();
    }

    /**
     * Met à jour le contenu de l'édit text dénombrement en fonction du total du genre. Si inférieur, le dénombrement est ramené au total
     * du nombre de genre. Si égal, le dénombrement suit les changement des dénombreemnt de genre
     *
     * @param oldNbGenre Ancien total du nombre de genre avant modification par l'utilisateur
     */
    protected void updateDenombrementETViaNbGenre(int oldNbGenre, RNFInventaire inv, EditText nombre) {
        int nbGenre = inv.getNbGenre();
        int nb = inv.getNombre();
        boolean isInchoherantNb = nb < nbGenre;

        if(oldNbGenre > nbGenre && oldNbGenre == nb)
            nb -= (oldNbGenre - nbGenre);
        else {
            if (isInchoherantNb)
                nb = adjustNombre(inv);
        }
        String newString = "";
        if(nb > 0)
            newString += nb;
        nombre.setText(newString);
    }

    /**
     * Décrémente la valeure contenue dans un EditText, utile pour les différents dénombrements
     *
     * @param et L'editText à changer
     * @param nb La valeur à modifier
     * @return La valeur décrémenté de 1
     */
    protected int decreaseDecompteEditText(EditText et, int nb){
        if(nb > 0) {
            String newText = "";
            nb--;
            if(nb > 0)
                newText += nb;
            et.setText(newText);
        }
        return nb;
    }

    private int adjustNombre(RNFInventaire inv){
        int nbGenre = inv.getNbGenre();
        int nb = inv.getNombre();
        if(nb < nbGenre)
            nb += nbGenre - nb;
        inv.setNombre(nb);
        return nb;
    }

    /**
     * Incrémente le dénombrement total de 1 et effectue une modification supplémentaire si le dénombrement est incohérant
     */
    protected void incNombre(RNFInventaire inv, EditText nombre){
        adjustNombre(inv);
        int nb = inv.getNombre();
        nb ++;

        nombre.setText(nb + "");
    }

    /**
     * Décrémente le dénombrement total de 1
     */
    protected void decNombre(RNFInventaire inv, EditText nombre){
        decreaseDecompteEditText(nombre,inv.getNombre());
    }

    /**
     * Incrémente de 1 le nombre de mâle
     */
    protected void incNbMale(RNFInventaire inv, EditText nbMaleText){
        int nbMale = inv.getNbMale();
        nbMale ++ ;

        nbMaleText.setText(nbMale + "");
    }

    /**
     * Décrémente de 1 le nombre de mâle
     */
    private void decNbMale(RNFInventaire inv, EditText nbMaleText){
       decreaseDecompteEditText(nbMaleText,inv.getNbMale());
    }

    /**
     * Incrémente de 1 le nombre de femelle
     */
    private void incNbFemale(RNFInventaire inv, EditText nbFemaleText){
        int nbFemale = inv.getNbFemale();
        nbFemale ++;

        nbFemaleText.setText(nbFemale + "");
    }

    /**
     * Décrémente de 1 le nombre de femelle
     */
    private void decNbFemale(RNFInventaire inv, EditText nbFemaleText){
        decreaseDecompteEditText(nbFemaleText,inv.getNbFemale());
    }

    public boolean allDenombrementAreCoherent(){
        for(RNFInventaire inv : items){
            if(!inv.hasCoherentDenombrement())
                return false;
        } return true;
    }

    public Filter getLatFilter() {
        if(filterLat == null)
            filterLat = new RNFInvLatinFilter();
        return filterLat;
    }

    public Filter getFrFilter() {
        if(filterFr == null)
            filterFr = new RNFInvFrFilter();
        return filterFr;
    }

    private abstract class RNFInvFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if(prefix == null || prefix.length() == 0){
                ArrayList<RNFInventaire> list = new ArrayList<>(original);
                results.values = list;
                results.count = list.size();
            }else {
                final ArrayList<RNFInventaire> list = new ArrayList<>(original);
                final ArrayList<RNFInventaire> nlist = new ArrayList<>();

                for(RNFInventaire inv : list){
                    final String invName = getGoodNameFromInv(inv).toLowerCase();
                    if(invName.startsWith(prefix))
                        nlist.add(inv);
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        protected abstract String getGoodNameFromInv(RNFInventaire inv);

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fitems = (ArrayList<RNFInventaire>)results.values;

            clear();
            for(RNFInventaire inv : fitems){
                add(inv);
            }
        }
    }

    private class RNFInvLatinFilter extends RNFInvFilter {

        @Override
        protected String getGoodNameFromInv(RNFInventaire inv) {
            return inv.getNomLatin();
        }
    }

    private class RNFInvFrFilter extends RNFInvFilter {

        @Override
        protected String getGoodNameFromInv(RNFInventaire inv) {
            return inv.getNomFr();
        }
    }

    private class RNFInventaireViewHolder {
        public TextView nomEspece;
        public EditText nombre, nbMale, nbFemale;
        public Button decNombreButton, incNombreButton, decNbMaleButton, incNbMaleButton, decNbFemaleButton, incNbFemaleButton;
    }
}
