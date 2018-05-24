package com.example.florian.myapplication.Activities.MapsActivities.Releve;

import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.R;

public class HistoryActivity extends AppCompatActivity {

    protected ListView listReleves;
    protected HistoryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new HistoryDao(this);
        dao.open();

        setContentView(R.layout.activity_history);
        listReleves = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
