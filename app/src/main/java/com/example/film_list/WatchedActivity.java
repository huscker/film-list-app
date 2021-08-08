package com.example.film_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class WatchedActivity extends AppCompatActivity {

    MyCSV fileHandler;
    int scrollPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show_activity();
        fileHandler = new MyCSV();
    }

    @Override
    protected void onStart() {
        super.onStart();
        show_activity();
    }

    void show_activity(){
        setContentView(R.layout.activity_watched);
        ArrayList<MyDataType> data = new ArrayList<>();

        // load data
        try {
            data = fileHandler.read(getBaseContext().openFileInput("films.csv"));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        LinearLayout main = (LinearLayout)findViewById(R.id.main_layout);

        // handle menu button
        Button add_btn = (Button)(findViewById(R.id.add_btn));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_menu_activity();
            }
        });

        // generate ui according to database
        int l = 0;
        for (int i = 0;i<data.size();i++){
            if(data.get(i).watched){
                // generate new item
                main = (LinearLayout)View.inflate(WatchedActivity.this,R.layout.item_watched,main);
                final LinearLayout item = (LinearLayout)(((ConstraintLayout)(main.getChildAt(l))).getChildAt(0));
                switch (data.get(i).liked){
                    case 0:
                        ((Button)(item.findViewWithTag("like"))).setBackgroundResource(R.drawable.ic_no_like);
                        break;
                    case 1:
                        ((Button)(item.findViewWithTag("like"))).setBackgroundResource(R.drawable.ic_like2);
                        break;
                    case 2:
                        ((Button)(item.findViewWithTag("like"))).setBackgroundResource(R.drawable.ic_like);
                        break;
                    default:
                        ((Button)(item.findViewWithTag("like"))).setBackgroundResource(R.drawable.ic_no_like);
                } // set liked

                final Button like_btn = (Button)(item.findViewWithTag("like"));
                like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScrollView sc = (ScrollView)findViewById(R.id.scroll_watched);
                        scrollPosition = sc.getScrollY();
                        LinearLayout item = (LinearLayout) view.getParent().getParent();
                        ArrayList<MyDataType> t = new ArrayList<>();

                        // load data
                        try {
                            t = fileHandler.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {

                                if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && j.watched){
                                    if(j.liked == 0 || j.liked == 2){
                                        t.set(t.indexOf(j),new MyDataType(
                                                j.name,j.comment,j.watched,1));
                                    }else{
                                        t.set(t.indexOf(j),new MyDataType(
                                                j.name,j.comment,j.watched,2));
                                    }
                                } // change to liked or disliked
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        // save data
                        try {
                            fileHandler.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        show_activity();

                    }
                });

                // handle long click on like button
                like_btn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ScrollView sc = (ScrollView)findViewById(R.id.scroll_watched);
                        scrollPosition = sc.getScrollY();
                        LinearLayout item = (LinearLayout) view.getParent().getParent();
                        ArrayList<MyDataType> t = new ArrayList<>();

                        // load data
                        try {
                            t = fileHandler.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {
                                if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && j.watched){
                                    Log.d("layout",j.name +"!Q@!@E!@");
                                    t.set(t.indexOf(j),new MyDataType(
                                            j.name,j.comment,j.watched,0));
                                } // set to neutral
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        // save data
                        try {
                            fileHandler.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        show_activity();
                        return true;
                    }
                });

                // remove item
                item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ScrollView sc = (ScrollView)findViewById(R.id.scroll_watched);
                        scrollPosition = sc.getScrollY();
                        final LinearLayout item = (LinearLayout) view;
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(WatchedActivity.this);
                        alertBuilder.setTitle("Подтверди намерение");
                        alertBuilder.setMessage("Точно удалить?");
                        alertBuilder.setCancelable(false);
                        alertBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList<MyDataType> t = new ArrayList<>();

                                // load data
                                try {
                                    t = fileHandler.read(getBaseContext().openFileInput("films.csv"));
                                    for (MyDataType j :
                                            t) {
                                        if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && j.watched){
                                            t.remove(j);
                                        } // remove
                                    }
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }

                                // save data
                                try {
                                    fileHandler.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
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

                // view comment
                final Button view_comment_btn = (Button)(item.findViewWithTag("view_comment_btn"));
                view_comment_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScrollView sc = (ScrollView)findViewById(R.id.scroll_watched);
                        scrollPosition = sc.getScrollY();
                        String name = ((TextView)item.findViewWithTag("text")).getText().toString();
                        String comment = "";
                        ArrayList<MyDataType> t = new ArrayList<>();

                        // load data
                        try {
                            t = fileHandler.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {
                                if(j.name.equals (name) && j.watched){
                                    comment = j.comment;
                                } // get data for activity
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        start_view_activity(comment);
                    }
                });

                // edit activity
                final Button edit_btn = (Button)(item.findViewWithTag("edit_btn"));
                edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScrollView sc = (ScrollView)findViewById(R.id.scroll_watched);
                        scrollPosition = sc.getScrollY();
                        String name = ((TextView)item.findViewWithTag("text")).getText().toString();
                        String comment = "";
                        ArrayList<MyDataType> t = new ArrayList<>();

                        // load data
                        try {
                            MyCSV temp = new MyCSV();
                            t = temp.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {
                                if(j.name.equals (name) && j.watched){
                                    comment = j.comment;
                                } // get data for activity
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        start_edit_activity(name,comment,true);
                    }
                });
                ((TextView)(item.findViewWithTag("text"))).setText(data.get(i).name);
                l++;
            }
        }
        final ScrollView sv = (ScrollView) findViewById(R.id.scroll_watched);
        sv.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, (int) scrollPosition);
            }
        },300);
    }
    void start_menu_activity(){
        Intent i = new Intent(this,BackupMenu.class);
        startActivity(i);
    }
    void start_edit_activity(String name,String comment,Boolean watched){
        Intent i = new Intent(this,EditFilm.class);
        i.putExtra("name",name);
        i.putExtra("comment",comment);
        i.putExtra("watched",watched);
        startActivity(i);
    }
    void start_view_activity(String comment){
        Intent i = new Intent(this,ViewComment.class);
        i.putExtra("comment",comment);
        startActivity(i);
    }
}