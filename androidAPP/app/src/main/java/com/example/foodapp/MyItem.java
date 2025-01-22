package com.example.foodapp;

public class MyItem {
    private String text;
    private int imageResId;
    private int percent;

    public MyItem(String text, int imageResId,int percent) {
        this.text = text;
        this.imageResId = imageResId;
        this.percent = percent;
    }

    public String getText() {
        return text;
    }

    public int getImageResId() {
        return imageResId;
    }

    public  int getPercent(){ return percent;}

    public  void setPercent(int per){
        percent = per;
    }
}
