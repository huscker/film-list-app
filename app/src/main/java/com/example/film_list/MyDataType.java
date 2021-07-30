package com.example.film_list;

public class MyDataType {
    public final String name;
    public final String comment;
    public final Boolean watched;
    public final Integer liked;
    MyDataType(String name,String comment,Boolean s,Integer t){
        this.name = name;
        this.comment = comment;
        this.watched = s;
        this.liked = t;
    }
    /*
    liked:
    0 ?
    1 liked
    2 disliked
     */
}
