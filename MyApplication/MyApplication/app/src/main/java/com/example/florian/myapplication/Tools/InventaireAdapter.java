package com.example.florian.myapplication.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;

import java.util.List;

public class InventaireAdapter extends ArrayAdapter<Inventaire>{

    public InventaireAdapter(Context context, List<Inventaire> inventaires) {
        super(context, 0, inventaires);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_inventaires,parent, false);
        }

        InventaireAdapter.InventaireViewHolder viewHolder = (InventaireAdapter.InventaireViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new InventaireAdapter.InventaireViewHolder();
            viewHolder.nomEspece = (TextView) convertView.findViewById(R.id.nomEspecInv);
            viewHolder.denombrement = (TextView) convertView.findViewById(R.id.denombrementInv);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateInv);
            //viewHolder.heure = (TextView) convertView.findViewById(R.id.heureReleve);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Inventaire inv = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.nomEspece.setText(inv.getNomFr());
        viewHolder.denombrement.setText(inv.getNombre() + "");
        viewHolder.date.setText(inv.getDate());
        //viewHolder.heure.setText(rel.getHeure());

        return convertView;
    }

    private class InventaireViewHolder {
        public TextView nomEspece;
        public TextView denombrement;
        public TextView date;
        //public TextView heure;
    }

}
