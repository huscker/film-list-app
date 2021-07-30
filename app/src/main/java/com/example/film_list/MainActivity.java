package com.example.film_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 23) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_DENIED) {
                this.finish();
                System.exit(0);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    23);
        }
        super.onCreate(savedInstanceState);
        MyCSV temp = new MyCSV();
        /*
        try {
            temp.clear(getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
        show_activity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        show_activity();

    }

    void show_activity(){
        setContentView(R.layout.activity_main);
        ArrayList<MyDataType> data = new ArrayList<>();
        try {
            MyCSV temp = new MyCSV();
            data = temp.read(getBaseContext().openFileInput("films.csv"));
            for (MyDataType i :
                    data) {
                Log.d("layout",i.name +i.comment+i.watched.toString()+i.liked.toString());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        LinearLayout main = (LinearLayout)findViewById(R.id.main_layout);
        main.removeAllViews();
        Button add_btn = (Button)(findViewById(R.id.add_btn));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_menu_activity();
            }
        });
        int l = 0;
        for (int i = 0;i<data.size();i++){
            if(!data.get(i).watched){
                main = (LinearLayout)View.inflate(MainActivity.this,R.layout.item_unwatched,main);
                final LinearLayout item = (LinearLayout)(((ConstraintLayout)(main.getChildAt(l))).getChildAt(0));
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final LinearLayout item = (LinearLayout) view;
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertBuilder.setTitle("Подтверди намерение");
                        alertBuilder.setMessage("Точно посмотрел?");
                        alertBuilder.setCancelable(false);
                        alertBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList<MyDataType> t = new ArrayList<>();
                                try {
                                    MyCSV temp = new MyCSV();
                                    t = temp.read(getBaseContext().openFileInput("films.csv"));
                                    for (MyDataType j :
                                            t) {
                                        if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && !j.watched){
                                            t.set(t.indexOf(j),new MyDataType(
                                                    j.name,j.comment,true,j.liked
                                            ));
                                        }
                                    }
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                try {
                                    MyCSV temp = new MyCSV();
                                    temp.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                show_activity();
                            }
                        });
                        alertBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("layout","NO");
                                show_activity();
                            }
                        });
                        AlertDialog alertDialog = alertBuilder.create();
                        alertDialog.show();
                    }
                });
                item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        final LinearLayout item = (LinearLayout) view;
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertBuilder.setTitle("Подтверди намерение");
                        alertBuilder.setMessage("Точно удалить?");
                        alertBuilder.setCancelable(false);
                        alertBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList<MyDataType> t = new ArrayList<>();
                                try {
                                    MyCSV temp = new MyCSV();
                                    t = temp.read(getBaseContext().openFileInput("films.csv"));
                                    for (MyDataType j :
                                            t) {
                                        if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && !j.watched){
                                            t.remove(j);
                                        }
                                    }
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                try {
                                    MyCSV temp = new MyCSV();
                                    temp.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                show_activity();
                            }
                        });
                        alertBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("layout","NO");
                                show_activity();
                            }
                        });
                        AlertDialog alertDialog = alertBuilder.create();
                        alertDialog.show();
                        return true;
                    }
                });
                final Button view_comment_btn = (Button)(item.findViewWithTag("view_comment_btn"));
                view_comment_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = ((TextView)item.findViewWithTag("text")).getText().toString();
                        String comment = "";
                        ArrayList<MyDataType> t = new ArrayList<>();
                        try {
                            MyCSV temp = new MyCSV();
                            t = temp.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {
                                if(j.name.equals (name) && !j.watched){
                                    comment = j.comment;
                                }
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        start_view_activity(comment);
                    }
                });
                final Button edit_btn = (Button)(item.findViewWithTag("edit_btn"));
                edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = ((TextView)item.findViewWithTag("text")).getText().toString();
                        String comment = "";
                        ArrayList<MyDataType> t = new ArrayList<>();
                        try {
                            MyCSV temp = new MyCSV();
                            t = temp.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {
                                if(j.name.equals (name) && !j.watched){
                                    comment = j.comment;
                                }
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        start_edit_activity(name,comment);
                    }
                });
                ((TextView)(item.findViewWithTag("text"))).setText(data.get(i).name);
                l++;
            }
        }
    }
    void start_menu_activity(){
        Intent i = new Intent(this,BackupMenu.class);
        startActivity(i);
    }
    void start_edit_activity(String name,String comment){
        Intent i = new Intent(this,EditFilm.class);
        i.putExtra("name",name);
        i.putExtra("comment",comment);
        startActivity(i);
    }
    void start_view_activity(String comment){
        Intent i = new Intent(this,ViewComment.class);
        i.putExtra("comment",comment);
        startActivity(i);
    }
}