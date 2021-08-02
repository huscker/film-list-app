package com.example.film_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BackupMenu extends AppCompatActivity {

    private MyCSV fileHandler;

    // action picker result handler
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // export
        if (requestCode == 58) {
            if (resultCode==RESULT_OK && data!=null && data.getData()!=null) {
                Uri uri = data.getData();
                try {

                    // relay data from internal storage to external
                    fileHandler.savePublic(fileHandler.read(getBaseContext().openFileInput("films.csv")),this.getContentResolver().openOutputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        //import
        if(requestCode == 61){
            if (resultCode==RESULT_OK && data!=null && data.getData()!=null) {
                Uri uri = data.getData();
                ArrayList<MyDataType> t = new ArrayList<MyDataType>();

                // load data from external storage
                try {
                    t = fileHandler.readPublic(this.getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // save imported database
                try {
                    fileHandler.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        fileHandler = new MyCSV();

        // input handler
        Button goto_watched = (Button)findViewById(R.id.goto_watched);
        goto_watched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_watched_activity();
            }
        }); // view watched films

        Button goto_unwatched = (Button)findViewById(R.id.goto_unwatched);
        goto_unwatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_unwatched_activity();
            }
        }); // view unwatched films

        Button goto_add_film = (Button)findViewById(R.id.goto_add_film);
        goto_add_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_add_activity();
            }
        });
        Button export_btn = (Button)findViewById(R.id.export_btn);
        export_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start action picker
                Intent theIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                theIntent.addCategory(Intent.CATEGORY_OPENABLE);
                theIntent.setType("text/plain");
                theIntent.putExtra(Intent.EXTRA_TITLE, "film_db_backup");
                try {
                    startActivityForResult(theIntent,58);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); // add film

        Button import_btn = (Button)findViewById(R.id.import_btn);
        import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start action picker
                Intent theIntent = new Intent(Intent.ACTION_GET_CONTENT);
                theIntent.addCategory(Intent.CATEGORY_OPENABLE);
                theIntent.setType("text/plain");
                theIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

                try {
                    startActivityForResult(theIntent,61);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); // import database

        Button clear_btn = (Button)findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BackupMenu.this);
                alertBuilder.setTitle("Подтверди намерение");
                alertBuilder.setMessage("Точно очистить всю базу данных?");
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyCSV temp = new MyCSV();
                        try {
                            temp.clear(getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("layout","NO");
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        }); // clear database

        ImageButton back_btn = (ImageButton)findViewById(R.id.go_go_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); // go back
    }
    void start_add_activity(){
        Intent i = new Intent(this, AddFilm.class);
        startActivity(i);
    }
    void start_watched_activity(){
        Intent i = new Intent(this, WatchedActivity.class);
        startActivity(i);
    }
    void start_unwatched_activity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
