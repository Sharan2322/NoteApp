package com.example.noteapp3;

public class firebaseModle {

    private String title;
    private String content;


    public firebaseModle(){

    }

    public firebaseModle(String title, String content){
        this.title = title;
        this.content = content;
    }




    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

