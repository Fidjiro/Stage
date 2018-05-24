package com.example.florian.myapplication.Activities.MapsActivities.Releve;

import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.florian.myapplication.R;

public class HistoryActivity extends AppCompatActivity {

    protected ListView listReleves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listReleves = (ListView) findViewById(R.id.listView);
    }
}
