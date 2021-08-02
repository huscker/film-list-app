package com.example.film_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class AddFilm extends AppCompatActivity {

    private MyCSV fileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);
        fileHandler = new MyCSV();

        // Input handlers
        ImageButton button = (ImageButton)findViewById(R.id.go_back);
        ImageButton button2 = (ImageButton)findViewById(R.id.apply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); // go back
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // add new film
                ArrayList<MyDataType> data = new ArrayList<>();
                EditText name = (EditText)(findViewById(R.id.editTextTextPersonName));
                EditText comment = (EditText)findViewById(R.id.editTextTextMultiLine);
                CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);

                // load database
                try {
                    data = fileHandler.read(getBaseContext().openFileInput("films.csv"));

                    // add new film
                    data.add(new MyDataType(name.getText().toString(),comment.getText().toString(),checkBox.isChecked(),0));
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                // save modified database
                try {
                    fileHandler.save(data,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                finish();
            }
        });
    }
}