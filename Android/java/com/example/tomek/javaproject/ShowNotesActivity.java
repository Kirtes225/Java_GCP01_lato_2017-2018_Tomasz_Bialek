package com.example.tomek.javaproject;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

public class ShowNotesActivity extends AppCompatActivity {

    private TextView addNewNote;
    private String path = Environment.getExternalStorageDirectory().toString() + "/Project Java/Notes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addNewNote = (TextView) findViewById(R.id.addNewNoteTV);
        addNewNote.setText(getAllContent());

    }


    private String getAllContent() {
        File file;
        String [] paths = new String[0];
        try{
            file = new File(path);
            paths = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        for(int i = paths.length - 1; i>=0; i--){
            try {
                sb.append(new Date(new File(path+paths[i]).lastModified()).toString());
                sb.append("\n\n\n");
                sb.append(Files.toString(new File(path+paths[i]), Charsets.UTF_8));
                sb.append("\n\n\n");

            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }


        return sb.toString();
    }
}
