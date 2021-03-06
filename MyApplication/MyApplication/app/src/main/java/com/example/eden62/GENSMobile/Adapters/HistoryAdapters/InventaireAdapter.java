package com.example.eden62.GENSMobile.Adapters.HistoryAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Stocker.InventoryStocker;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.util.List;

/**
 * Adapter d'inventaires
 */
public class InventaireAdapter extends ItemsAdapter<InventoryStocker,Inventaire> {

    public InventaireAdapter(Context context, List<Inventaire> inventaires) {
        super(context, inventaires);
        checkedItemsStocker = new InventoryStocker(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_inventaires,parent, false);

        InventaireViewHolder viewHolder = (InventaireViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new InventaireViewHolder();
            viewHolder.nomEspece = (TextView) convertView.findViewById(R.id.nomEspeceInv);
            viewHolder.denombrement = (TextView) convertView.findViewById(R.id.denombrementInv);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateInv);
            viewHolder.heure = (TextView) convertView.findViewById(R.id.heureInventaire);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.itemCheckbox);
            allCheckBoxes.add(viewHolder.checkBox);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la liste des inventaires
        final Inventaire inv = getItem(position);

        String nom;
        String nomFr= inv.getNomFr();
        //il ne reste plus qu'à remplir notre vue
        nom = inv.getNomLatin();
        int nv = inv.getNv_taxon();

        nom = addSpToString(nv,nom);

        setNiceColorToView(nv, viewHolder.nomEspece);

        if(!nomFr.isEmpty())
            nom += " - " + nomFr;

        viewHolder.nomEspece.setText(nom);
        int nb = inv.getNombre();
        // Si le dénombrement n'a pas été défini
        if(nb == 0)
            viewHolder.denombrement.setText("Présence");
        else
            viewHolder.denombrement.setText(nb + "");
        viewHolder.date.setText(Utils.printDateWithYearIn2Digit(inv.getDate()));
        viewHolder.heure.setText(inv.getHeure());

        // Si l'inventaire est hors-site, on met un fond rouge, sinon un fond transparent
        if(!inv.isToSync())
            convertView.setBackgroundColor(Color.RED);
        else
            convertView.setBackgroundColor(Color.TRANSPARENT);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    checkedItemsStocker.add(inv);
                else
                    checkedItemsStocker.remove(inv);
            }
        });

        if(checkedItemsStocker.getCheckedItems().contains(inv))
            viewHolder.checkBox.setChecked(true);
        else
            viewHolder.checkBox.setChecked(false);

        return convertView;
    }

    // ajoute l'extension sp. au nom latin de l'espèce si son niveau est 5
    private String addSpToString(int nv, String name){
        if(nv == 5)
            return name + " sp.";
        return name;
    }

    // Affecte une couleur au texte en fonction du niveau de l'espèce
    private void setNiceColorToView(int nv, TextView view){
        int color;
        if(nv == 5){
            color = Color.BLUE;
        } else if(nv == 6)
            color = Color.BLACK;
        else if(nv == 7)
            color = Color.GRAY;
        else
            color = Color.rgb(255,140,0);
        view.setTextColor(color);
    }

    private class InventaireViewHolder {
        public TextView nomEspece, denombrement, date, heure;
        public CheckBox checkBox;
    }
}
