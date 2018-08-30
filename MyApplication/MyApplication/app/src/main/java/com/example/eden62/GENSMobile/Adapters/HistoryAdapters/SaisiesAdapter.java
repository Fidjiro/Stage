package com.example.eden62.GENSMobile.Adapters.HistoryAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Stocker.SaisiesStocker;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.util.List;

public class SaisiesAdapter extends ItemsAdapter<SaisiesStocker,CampagneProtocolaire> {

    public SaisiesAdapter(Context context, List<CampagneProtocolaire> items) {
        super(context, items);
        checkedItemsStocker = new SaisiesStocker(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_protocole_campagne,parent,false);

        CampagneProtocolaireViewHolder viewHolder = (CampagneProtocolaireViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new CampagneProtocolaireViewHolder();
            viewHolder.nomCampagne = (TextView)convertView.findViewById(R.id.nomCampagne);
            viewHolder.nomProto = (TextView)convertView.findViewById(R.id.nomProto);
            viewHolder.nomSite = (TextView)convertView.findViewById(R.id.nomSite);
            viewHolder.date = (TextView)convertView.findViewById(R.id.dateCampagne);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckbox);
            allCheckBoxes.add(viewHolder.checkbox);

            convertView.setTag(viewHolder);
        }

        final CampagneProtocolaire campagne = getItem(position);

        viewHolder.nomCampagne.setText(campagne.getName());
        viewHolder.nomProto.setText(campagne.getNomProto());
        viewHolder.nomSite.setText(campagne.getNomSite());
        viewHolder.date.setText(Utils.printDateWithYearIn2Digit(campagne.getDate()));
        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    checkedItemsStocker.add(campagne);
                else
                    checkedItemsStocker.remove(campagne);
            }
        });

        if(checkedItemsStocker.getCheckedItems().contains(campagne))
            viewHolder.checkbox.setChecked(true);
        else
            viewHolder.checkbox.setChecked(false);

        return convertView;
    }

    private class CampagneProtocolaireViewHolder {
        public TextView nomCampagne, nomProto, nomSite, date;
        public CheckBox checkbox;
    }
}
