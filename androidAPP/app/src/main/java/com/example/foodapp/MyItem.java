package com.example.foodapp;

public class MyItem {
    private String text;
    private int imageResId;

    public MyItem(String text, int imageResId) {
        this.text = text;
        this.imageResId = imageResId;
    }

    public String getText() {
        return text;
    }

    public int getImageResId() {
        return imageResId;
    }
}
