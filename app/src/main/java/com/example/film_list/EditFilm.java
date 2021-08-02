package com.example.film_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class EditFilm extends AppCompatActivity {

    MyCSV fileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);
        fileHandler = new MyCSV();

        // change title if in edit activity
        TextView title = (TextView)findViewById(R.id.title_add_or_edit);
        title.setText("Изменить фильм");

        // set returned data
        final String name = getIntent().getStringExtra("name");
        final String comment = getIntent().getStringExtra("comment");
        ImageButton button = (ImageButton)findViewById(R.id.go_back);
        ImageButton button2 = (ImageButton)findViewById(R.id.apply);
        EditText name_text = (EditText)(findViewById(R.id.editTextTextPersonName));
        name_text.setText(name);
        EditText comment_text = (EditText)findViewById(R.id.editTextTextMultiLine);
        comment_text.setText(comment);
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setChecked(getIntent().getBooleanExtra("watched",false));

        // input handler
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); // go back

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyDataType> data = new ArrayList<>();
                EditText name_text = (EditText)(findViewById(R.id.editTextTextPersonName));
                EditText comment_text = (EditText)findViewById(R.id.editTextTextMultiLine);
                CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
                // load data
                try {
                    data = fileHandler.read(getBaseContext().openFileInput("films.csv"));
                    for (MyDataType j :
                            data) {
                        if(j.name.equals (name)){
                            data.set(data.indexOf(j),new MyDataType(
                                    name_text.getText().toString(),
                                    comment_text.getText().toString(),
                                    checkBox.isChecked(),
                                    j.liked
                            )); // update film
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                // save data
                try {
                    fileHandler.save(data,getBaseContext().openFileOutput("films.csv",Context.MODE_PRIVATE));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                finish();
            }
        }); // edit film
    }
}