package com.example.film_list;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyCSV {

    // standart file reader/writer
    // csv generated using symbols: Ȩ≝
    // as delimiters

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
                if(i.length() > 1){
                    list.add(new MyDataType(i.split("≝")[0],i.split("≝")[1],Boolean.parseBoolean(i.split("≝")[2]),Integer.parseInt(i.split("≝")[3])));
                }
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
                    line += i.name +"≝" +i.comment +"≝" + i.watched.toString() +"≝" +i.liked.toString()+ "Ȩ";
                }
                fileOutputStream.write(line.getBytes());
                fileOutputStream.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void savePublic(ArrayList<MyDataType> data, OutputStream output){
        try {
            if (data.size() > 0){
                String line = "";
                for (MyDataType i : data
                ) {
                    line += i.name +"≝" +i.comment +"≝" + i.watched.toString() +"≝" +i.liked.toString()+ "Ȩ";
                }
                output.write(line.getBytes());
                output.flush();
                output.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<MyDataType> readPublic(InputStream inputStream){
        ArrayList<MyDataType> list = new ArrayList<>();
        String line = new String();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (bufferedReader.ready()){
                line += bufferedReader.readLine()+"\n";
            }
            String[] data = line.split("Ȩ");
            for (String i :
                    data) {
                if(i.length() > 1){
                    list.add(new MyDataType(i.split("≝")[0],i.split("≝")[1],Boolean.parseBoolean(i.split("≝")[2]),Integer.parseInt(i.split("≝")[3])));
                }
            }
            inputStream.close();
            bufferedReader.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            return list;
        }
        return list;


    }
    public void clear(FileOutputStream fileOutputStream){
        try {
                fileOutputStream.write("".getBytes());
                fileOutputStream.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
