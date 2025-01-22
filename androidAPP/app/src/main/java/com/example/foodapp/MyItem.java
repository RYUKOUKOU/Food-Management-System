package com.example.foodapp;

import java.util.HashMap;
import java.util.Map;

public class MyItem {
    private final String name;
    private int imageResId;
    private int percent;
    private int shelfLife;

    private static final Map<String, Integer> nameToImageMap = new HashMap<>();
    static {
        nameToImageMap.put("Apple", R.drawable.item_images_apple);
        nameToImageMap.put("Banana", R.drawable.banana);
        nameToImageMap.put("Broccoli", R.drawable.broccoli);
        nameToImageMap.put("Cabbage", R.drawable.cabbage);
        nameToImageMap.put("Carrot", R.drawable.carrot);
        nameToImageMap.put("Cucumber", R.drawable.grape);
        nameToImageMap.put("Grape", R.drawable.grape);
        nameToImageMap.put("Lemon", R.drawable.lemon);
        nameToImageMap.put("Orange", R.drawable.orange);
        nameToImageMap.put("Potato", R.drawable.potato);
    }

    public MyItem(String text, int shelfLife) {
        this.name = text;
        this.shelfLife = shelfLife;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getShelfLife(){return shelfLife;}

    public  int getPercent(){ return percent;}

    public  void setPercent(int per){
        percent = per;
    }
}
