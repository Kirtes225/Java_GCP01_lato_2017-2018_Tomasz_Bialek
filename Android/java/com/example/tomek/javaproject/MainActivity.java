package com.example.tomek.javaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void click(View view) {
        Intent intent = null;
        switch (view.getId())
        {
            case R.id.weatherBtn:
                intent = new Intent(MainActivity.this, WeatherActivity.class);
                break;
            case R.id.notesBtn:
                intent = new Intent(MainActivity.this, NotesActivity.class);
                break;
        }
        startActivity(intent);
    }
}
