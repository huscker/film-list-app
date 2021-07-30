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
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class watched extends AppCompatActivity {

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
        Log.d("layout",this.getClass().toString());
        setContentView(R.layout.activity_watched);
        ArrayList<MyDataType> data = new ArrayList<>();
        try {
            MyCSV temp = new MyCSV();
            data = temp.read(getBaseContext().openFileInput("films.csv"));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        LinearLayout main = (LinearLayout)findViewById(R.id.main_layout);
        Button add_btn = (Button)(findViewById(R.id.add_btn));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_menu_activity();
            }
        });
        int l = 0;
        for (int i = 0;i<data.size();i++){
            if(data.get(i).watched){
                main = (LinearLayout)View.inflate(watched.this,R.layout.item_watched,main);
                final LinearLayout item = (LinearLayout)(((ConstraintLayout)(main.getChildAt(l))).getChildAt(0));
                 // <---------------------------------------------------
                Log.d("NAME",String.valueOf(data.get(i).liked));
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
                }
                final Button like_btn = (Button)(item.findViewWithTag("like"));
                like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LinearLayout item = (LinearLayout) view.getParent().getParent();
                        ArrayList<MyDataType> t = new ArrayList<>();
                        try {
                            MyCSV temp = new MyCSV();
                            t = temp.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {

                                if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && j.watched){
                                    Log.d("layout",j.name +"!Q@!@E!@");
                                    if(j.liked == 0 || j.liked == 2){ // <---------------------------------------------------------------------
                                        t.set(t.indexOf(j),new MyDataType(
                                                j.name,j.comment,j.watched,1));
                                    }else{
                                        t.set(t.indexOf(j),new MyDataType(
                                                j.name,j.comment,j.watched,2));
                                    }
                                }


                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        Log.d("layout",t.toString());
                        try {
                            MyCSV temp = new MyCSV();
                            temp.save(t,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        show_activity();

                    }
                });
                ((Button)(item.findViewWithTag("like"))).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        LinearLayout item = (LinearLayout) view.getParent().getParent();
                        ArrayList<MyDataType> t = new ArrayList<>();
                        try {
                            MyCSV temp = new MyCSV();
                            t = temp.read(getBaseContext().openFileInput("films.csv"));
                            for (MyDataType j :
                                    t) {
                                if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && j.watched){
                                    Log.d("layout",j.name +"!Q@!@E!@");
                                    t.set(t.indexOf(j),new MyDataType(
                                            j.name,j.comment,j.watched,0));
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
                        return true;
                    }
                });
                item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        final LinearLayout item = (LinearLayout) view;
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(watched.this);
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
                                        if(j.name.equals (((TextView)(item.findViewWithTag("text"))).getText().toString()) && j.watched){
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
                                if(j.name.equals (name) && j.watched){
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
                                if(j.name.equals (name) && j.watched){
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