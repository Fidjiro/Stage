package com.example.florian.myapplication.HistoryAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.Historiques.Stocker.ReleveStocker;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReleveAdapter extends ArrayAdapter<Releve> {

    private ReleveStocker checkedReleveStocker;
    public List<CheckBox> allCheckBoxes;

    public ReleveAdapter(Context context, List<Releve> releves) {
        super(context, 0, releves);
        checkedReleveStocker = new ReleveStocker(context);
        allCheckBoxes = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_releves,parent, false);
        }

        ReleveViewHolder viewHolder = (ReleveViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ReleveViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.nomRel);
            viewHolder.type = (TextView) convertView.findViewById(R.id.typeReleve);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateReleve);
            viewHolder.heure = (TextView) convertView.findViewById(R.id.heureReleve);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.importStatusImage);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.itemCheckbox);
            allCheckBoxes.add(viewHolder.checkBox);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        final Releve rel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.nom.setText(rel.getNom());
        viewHolder.type.setText(rel.getType());
        viewHolder.date.setText(Utils.printDateWithYearIn2Digit(rel.getDate()));
        viewHolder.heure.setText(rel.getHeure());
        if(rel.getImportStatus().equals("true"))
            viewHolder.image.setImageResource(R.drawable.check_oui);
        else
            viewHolder.image.setImageResource(R.drawable.to_sync);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    checkedReleveStocker.add(rel);
                else
                    checkedReleveStocker.remove(rel);
            }
        });
        return convertView;
    }

    private class ReleveViewHolder {
        public TextView nom;
        public TextView type;
        public TextView date;
        public TextView heure;
        public ImageView image;
        public CheckBox checkBox;
    }

    public ReleveStocker getCheckedReleveStocker() {
        return checkedReleveStocker;
    }
}
