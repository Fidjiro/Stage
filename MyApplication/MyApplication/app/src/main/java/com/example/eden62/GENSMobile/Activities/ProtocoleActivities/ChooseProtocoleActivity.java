package com.example.eden62.GENSMobile.Activities.ProtocoleActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.ChooseTransectActivity;
import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.RNFSite;
import com.example.eden62.GENSMobile.Database.RNFDatabase.Transect;
import com.example.eden62.GENSMobile.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseProtocoleActivity extends AppCompatActivity {

    protected Spinner protoSpinner,siteSpinner;
    protected static final String TOAST_MESSAGE = "Veuillez sélectionner un ";
    protected static final String PROTO_STRING = "protocole";
    protected static final String SITE_STRING = "site";
    protected List<Protocole> protocoles;
    protected List<Site> sites = new ArrayList<>();
    protected ArrayAdapter<Site> sitesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_protocole);

        setListOfProto();

        protoSpinner = (Spinner) findViewById(R.id.protocole_spinner);
        siteSpinner = (Spinner) findViewById(R.id.site_spinner);

        ArrayAdapter<Protocole> protoAdapter = new ArrayAdapter<Protocole>(this, android.R.layout.simple_spinner_item, protocoles){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item,parent, false);
                TextView t = (TextView) convertView.findViewById(android.R.id.text1);

                Protocole p = getItem(position);
                t.setText(p.getName());

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent, false);
                TextView t = (TextView) convertView.findViewById(android.R.id.text1);

                Protocole p = getItem(position);
                t.setText(p.getName());

                return convertView;
            }
        };

        sitesAdapter = new ArrayAdapter<Site>(this, android.R.layout.simple_spinner_item, sites){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item,parent, false);

                TextView t = (TextView) convertView.findViewById(android.R.id.text1);

                Site s = getItem(position);
                t.setText(s.getName());

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent, false);
                TextView t = (TextView) convertView.findViewById(android.R.id.text1);

                Site s = getItem(position);
                t.setText(s.getName());

                return convertView;
            }
        };

        protoSpinner.setAdapter(protoAdapter);
        Button validProto = (Button) findViewById(R.id.valider_proto);
        final LinearLayout siteContainer = (LinearLayout) findViewById(R.id.choixSiteContainer);

        protoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int visibility;
                if(isASelectedItem(protoSpinner)) {
                    setListOfSites();
                    visibility = View.VISIBLE;
                }
                else
                    visibility = View.GONE;
                siteContainer.setVisibility(visibility);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        validProto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(siteAndProtoAreChosen())
                    launchProto();
                else
                    actionWhenFieldsNotChosen();
            }
        });
    }

    private void setListOfSites() {
        sites = ((Protocole)protoSpinner.getSelectedItem()).getAvailableSites();
        sitesAdapter.addAll(sites);
        sitesAdapter.notifyDataSetChanged();
        siteSpinner.setAdapter(sitesAdapter);
    }

    protected void setListOfProto(){
        protocoles = new ArrayList<>();

        ArrayList<Transect> transects = new ArrayList<>();
        transects.add(new Transect("Transect 1",100));
        transects.add(new Transect("Transect 2",103));
        transects.add(new Transect("Transect 3",102));
        transects.add(new Transect("Transect 4",103));
        transects.add(new Transect("Transect 5",100));

        ArrayList<Transect> test = new ArrayList<>();
        test.add(new Transect("Transect test", 12));

        ArrayList<Site> sites = new ArrayList<>();
        //Ajout d'un site de nom vide pour avoir l'item vide dans le spinner
        sites.add(new Site(""));
        sites.add(new RNFSite("test",test));
        sites.add(new RNFSite("Mont Pelé et Hulin",transects));

        //Ajout d'un protocole de nom vide pour avoir l'item vide dans le spinner
        protocoles.add(new Protocole("",null,null));
        protocoles.add(new Protocole("RNF rhopalocères", sites, ChooseTransectActivity.class));
    }

    private void setErrorOnSelectedItem(Spinner s, String spinnerType){
        String msg = TOAST_MESSAGE + spinnerType;
        TextView selectedView = (TextView) s.getSelectedView();
        selectedView.setError(msg);
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }

    private void actionWhenFieldsNotChosen() {
        if(!isASelectedItem(protoSpinner)){
            setErrorOnSelectedItem(protoSpinner, PROTO_STRING);
            return;
        }
        if(!isASelectedItem(siteSpinner))
            setErrorOnSelectedItem(siteSpinner, SITE_STRING);
    }

    protected void launchProto(){
        Protocole selectedProto = (Protocole) protoSpinner.getSelectedItem();
        Intent intent = new Intent(this, selectedProto.getActivityToLaunch());
        if(selectedProto.getName().equals("RNF rhopalocères")){
            RNFSite site = (RNFSite) siteSpinner.getSelectedItem();
            intent.putParcelableArrayListExtra("transects",site.getTransects());
        }
        startActivity(intent);
    }

    protected boolean siteAndProtoAreChosen(){
        return isASelectedItem(protoSpinner) && isASelectedItem(siteSpinner);
    }

    protected boolean isASelectedItem(Spinner s){
        return !s.getSelectedItem().toString().equals("");
    }
}
