package com.example.film_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show_activity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        show_activity();

    }

    void show_activity(){
        setContentView(R.layout.activity_main);
        Log.d("layout",this.getClass().toString());
        ArrayList<MyDataType> data = new ArrayList<>();
        try {
            MyCSV temp = new MyCSV();
            data = temp.read(getBaseContext().openFileInput("films.csv"));
            for (MyDataType i :
                    data) {
                Log.d("layout",i.first+i.second.toString()+i.third.toString());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        /*
        try {
            MyCSV temp = new MyCSV();
            temp.save(data,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        */
        LinearLayout main = (LinearLayout)findViewById(R.id.main_layout);
        main.removeAllViews();
        Button add_btn = (Button)(findViewById(R.id.add_btn));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_add_activity();
            }
        });
        add_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_watched_activity();
                return true;
            }
        });
        int l = 0;
        for (int i = 0;i<data.size();i++){
            if(!data.get(i).second){
                main = (LinearLayout)View.inflate(MainActivity.this,R.layout.item_unwatched,main);
                LinearLayout item = (LinearLayout)(((ConstraintLayout)(main.getChildAt(l))).getChildAt(0));
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
                                        if(j.first.equals (((TextView)(item.getChildAt(0))).getText().toString()) && j.second == false && j.third == true){
                                            t.set(t.indexOf(j),new MyDataType(((TextView)(item.getChildAt(0))).getText().toString(),true,true));
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
                                        if(j.first.equals (((TextView)(item.getChildAt(0))).getText().toString()) && j.second == false && j.third == true){
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
                Log.d("layout",item.getChildAt(0).getTag().toString());
                ((TextView)(item.getChildAt(0))).setText(data.get(i).first);
                l++;
            }
        }
    }
    void start_add_activity(){
        Intent i = new Intent(this,add_film.class);
        startActivity(i);
    }
    void start_watched_activity(){
        Intent i = new Intent(this,watched.class);
        startActivity(i);
    }
}