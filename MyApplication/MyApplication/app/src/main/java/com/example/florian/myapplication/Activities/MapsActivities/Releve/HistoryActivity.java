package com.example.florian.myapplication.Activities.MapsActivities.Releve;

import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.ReleveAdapter;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    protected ListView listReleves;
    protected HistoryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new HistoryDao(this);
        dao.open();

        setContentView(R.layout.activity_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listReleves = (ListView) findViewById(R.id.listView);

        List<Releve> releves = dao.getReleveOfTheUsr(getCurrentUsrId());

        ReleveAdapter adapter = new ReleveAdapter(this,releves);
        listReleves.setAdapter(adapter);
    }

    protected long getCurrentUsrId(){
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return loginPreferences.getLong("usrId",0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
