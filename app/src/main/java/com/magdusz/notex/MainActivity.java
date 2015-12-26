package com.magdusz.notex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    public static final String DEBUGTAG = "MGD";
    public static final String TEXTFILE = "notex.txt";
    public static final String FILESAVED = "FileSaved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSaveButtonListener();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean fileSaved = prefs.getBoolean(FILESAVED, false);

        if (fileSaved) {
            loadSavedFile();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  //MENU
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.menu_passpoints_reset:
                Toast.makeText(this, "McPusz", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addLockButtonListener()
    {
        Button lockBtn = (Button)findViewById(R.id.lock);

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUGTAG, "start image activity");
                startActivity(new Intent(MainActivity.this, ImageActivity.class));
            }
        });

    }
    private void loadSavedFile(){
        try {

           FileInputStream fis = openFileInput(TEXTFILE);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new DataInputStream(fis)));

            EditText editText = (EditText)findViewById(R.id.text);

            String line;

            while((line = reader.readLine()) !=null) {
                editText.append(line);
                editText.append("\n");

            }

            fis.close();

        } catch (Exception e) {
            Log.d(DEBUGTAG, "Unable to read file");
        }
    }

    private void addSaveButtonListener(){
       Button saveBtn = (Button)findViewById(R.id.save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.text);

                String text = editText.getText().toString();

                Toast.makeText(MainActivity.this, getString(R.string.toast_cant_save), Toast.LENGTH_LONG).show();


                try {
                    FileOutputStream fos = openFileOutput(TEXTFILE,
                            Context.MODE_PRIVATE);
                    fos.write(text.getBytes());
                    fos.close();

                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(FILESAVED, true);
                    editor.commit();
                } catch (Exception e) {
                    Log.d(DEBUGTAG, "Unable to save file");
                }

                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(FILESAVED,true);

            }
        });
    }

}
