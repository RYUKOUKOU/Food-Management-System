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
        nameToImageMap.put("Apple", R.drawable.itemimages_apple);
        nameToImageMap.put("Banana", R.drawable.itemimages_banana);
        nameToImageMap.put("Broccoli", R.drawable.itemimages_broccoli);
        nameToImageMap.put("Cabbage", R.drawable.itemimages_cabbage);
        nameToImageMap.put("Carrot", R.drawable.itemimages_carrot);
        nameToImageMap.put("Cucumber", R.drawable.itemimages_cucumber);
        nameToImageMap.put("Grape", R.drawable.itemimages_grape);
        nameToImageMap.put("Lemon", R.drawable.itemimages_lemon);
        nameToImageMap.put("Orange", R.drawable.itemimages_orange);
        nameToImageMap.put("Potato", R.drawable.itemimages_potato);
    }

    public MyItem(String text, int shelfLife) {
        this.name = text;
        this.shelfLife = shelfLife;
        try{
            this.imageResId = nameToImageMap.get(name);
        }catch (Exception e) {
            System.out.println("食品名错误"+text);
        }
        this.percent = 100;
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
