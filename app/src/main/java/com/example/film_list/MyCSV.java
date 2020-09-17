package com.example.film_list;

import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyCSV {
    public MyCSV(){
    }
    public ArrayList<MyDataType> read(FileInputStream fileInputStream){
        ArrayList<MyDataType> list = new ArrayList<>();
        String line;
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while( (line = bufferedReader.readLine()) != null){
                list.add(new MyDataType(line.split("\t")[0],Boolean.parseBoolean(line.split("\t")[1]),Boolean.parseBoolean(line.split("\t")[2])));
            }
            fileInputStream.close();
            bufferedReader.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            return list;
        }
        return list;
    }
    public void save(ArrayList<MyDataType> data,FileOutputStream fileOutputStream){
        try {
            if (data.size() > 0){
                String line = "";
                for (MyDataType i : data
                ) {
                    line += i.first +"\t" + i.second.toString() +"\t" +i.third.toString()+ "\n";
                }
                fileOutputStream.write(line.getBytes());
                fileOutputStream.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
