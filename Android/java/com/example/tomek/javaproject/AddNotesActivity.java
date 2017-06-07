package com.example.tomek.javaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class AddNotesActivity extends AppCompatActivity {
    EditText et;
    Bundle bundle = new Bundle();
    private String path = Environment.getExternalStorageDirectory().toString() + "/Project Java/Notes";
    private final int MEMORY_ACCESS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("aktywność", "onCreate");
        setContentView(R.layout.activity_add_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et = (EditText) findViewById(R.id.editText);
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddNotesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
        }
        else{
            ActivityCompat.requestPermissions(AddNotesActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MEMORY_ACCESS);
        }
        et.setText(bundle.getString("et"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case MEMORY_ACCESS:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    Toast.makeText(getApplicationContext(), "You have to give a permission", Toast.LENGTH_LONG).show();
                }
        }
        //super.onRequestPermissionsResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save)
        {
            createDir();
            createFile();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createDir()
    {
        File folder = new File(path);
        if(!folder.exists())
        {
            try
            {
                folder.mkdirs();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createFile()
    {
        File file = new File(path+ "/" + System.currentTimeMillis()+ ".txt");
        FileOutputStream fOut;
        OutputStreamWriter myOutWriter;
        try
        {
            fOut = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(et.getText());
            myOutWriter.close();
            fOut.close();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        Log.d("Aktywność", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("Aktywność", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("Aktywność", "onPause");
        //text = et.getText().toString();
        bundle.putString("et", et.getText().toString());
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d("Aktywność", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d("Aktywność", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Aktywność", "onDestroy");
        super.onDestroy();
    }
}