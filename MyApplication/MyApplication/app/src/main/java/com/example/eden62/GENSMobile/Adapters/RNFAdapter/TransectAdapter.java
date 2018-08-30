package com.example.eden62.GENSMobile.Adapters.RNFAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.Transect;
import com.example.eden62.GENSMobile.R;

import java.util.List;

public class TransectAdapter extends ArrayAdapter<Transect> {

    public TransectAdapter(Context context, List<Transect> transects) {
        super(context, 0 , transects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_transect,parent, false);
        }

        TransectViewHolder viewHolder = (TransectViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TransectViewHolder();
            viewHolder.transectName = (TextView) convertView.findViewById(R.id.transectName);
            viewHolder.transectState = (ImageView) convertView.findViewById(R.id.transectState);
        }

        Transect transect = getItem(position);

        viewHolder.transectName.setText(transect.toString());
        if(transect.isDone())
            viewHolder.transectState.setImageResource(R.drawable.check_oui);
        else
            viewHolder.transectState.setImageResource(R.drawable.to_sync);

        return convertView;
    }

    private class TransectViewHolder{
        public TextView transectName;
        public ImageView transectState;
    }
}
