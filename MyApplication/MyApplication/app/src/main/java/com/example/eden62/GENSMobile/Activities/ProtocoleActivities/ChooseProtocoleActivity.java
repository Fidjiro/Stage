package com.example.eden62.GENSMobile.Activities.ProtocoleActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.ChooseTransectActivity;
import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.FormMeteoActivity;
import com.example.eden62.GENSMobile.R;

import java.util.HashMap;
import java.util.Map;

public class ChooseProtocoleActivity extends AppCompatActivity {

    protected Spinner protoSpinner,siteSpinner;
    protected static final String TOAST_MESSAGE = "Veuillez sélectionner un ";
    protected static final String PROTO_STRING = "protocole";
    protected static final String SITE_STRING = "site";
    protected Map<String,Class> protocoleActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_protocole);

        protocoleActivities = new HashMap<>();
        addEveryProtocoleInHashMap();

        protoSpinner = (Spinner) findViewById(R.id.protocole_spinner);
        siteSpinner = (Spinner) findViewById(R.id.site_spinner);
        Button validProto = (Button) findViewById(R.id.valider_proto);

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

    protected void addEveryProtocoleInHashMap(){
        protocoleActivities.put("RNF rhopalocères", FormMeteoActivity.class);
    }

    private void setErrorOnSelectedItem(Spinner s, String spinnerType){
        String msg = TOAST_MESSAGE + spinnerType;
        TextView selectedView = (TextView) s.getSelectedView();
        selectedView.setError(msg);
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }

    private void actionWhenFieldsNotChosen() {
        if(!isASelectedItem(siteSpinner))
            setErrorOnSelectedItem(siteSpinner, SITE_STRING);
        if(!isASelectedItem(protoSpinner))
            setErrorOnSelectedItem(protoSpinner, PROTO_STRING);
    }

    protected void launchProto(){
        String selectedProto = ((TextView)protoSpinner.getSelectedView()).getText().toString();
        startActivity(new Intent(this, protocoleActivities.get(selectedProto)));
    }

    protected boolean siteAndProtoAreChosen(){
        return isASelectedItem(protoSpinner) && isASelectedItem(siteSpinner);
    }

    protected boolean isASelectedItem(Spinner s){
        return !s.getSelectedItem().equals("");
    }
}
