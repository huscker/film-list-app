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
        String line = new String();
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (bufferedReader.ready()){
                line += bufferedReader.readLine()+"\n";
            }
            String[] data = line.split("Ȩ");
            for (String i :
                    data) {
                list.add(new MyDataType(i.split("≝")[0],Boolean.parseBoolean(i.split("≝")[1]),Boolean.parseBoolean(i.split("≝")[2])));
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
                    line += i.first +"≝" + i.second.toString() +"≝" +i.third.toString()+ "Ȩ";
                }
                fileOutputStream.write(line.getBytes());
                fileOutputStream.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
