package com.example.film_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class add_film extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);
        ImageButton button = (ImageButton)findViewById(R.id.go_back);
        ImageButton button2 = (ImageButton)findViewById(R.id.apply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyDataType> data = new ArrayList<>();
                EditText text = (EditText)(findViewById(R.id.editTextTextPersonName));
                try {
                    MyCSV temp = new MyCSV();
                    data = temp.read(getBaseContext().openFileInput("films.csv"));
                    data.add(new MyDataType(text.getText().toString(),false,true));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                try {
                    MyCSV temp = new MyCSV();
                    temp.save(data,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                finish();
            }
        });
    }
}