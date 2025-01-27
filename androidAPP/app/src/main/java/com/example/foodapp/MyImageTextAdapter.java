package com.example.foodapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyImageTextAdapter extends RecyclerView.Adapter<MyImageTextAdapter.MyViewHolder> {

    private static List<MyItem> itemList = null;
    private String listModel = MainActivity.listModel;
    private static List<Integer> selectedItems = new ArrayList<>();
    protected static Map<String, String> buttonSelections = new HashMap<>();
    public MyImageTextAdapter(List<MyItem> itemList) {
        this.itemList = itemList;
    }
    private String SuggesstText = "#選択された食材\n";
    private MyViewHolder holder;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyItem item = itemList.get(position);

        this.holder = holder;
        holder.textView.setText(item.getName());
        holder.textViewTime.setText(item.getShelfLife());
        holder.imageView.setImageResource(item.getImageResId());
        holder.circularProgressBar.setProgress(item.getPercent());

        // 設置按鈕容器的顯示/隱藏
        showButton(holder, position);
        resetButtonStyles(holder);

        // 设置选中状态
        if (selectedItems.contains(position)) {
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#d3d3d3")));
        } else {
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
        }

        // 处理点击事件
        holder.itemView.setOnClickListener(v -> {
            listModel = MainActivity.listModel;
            System.out.println(listModel);
            if (Objects.equals(listModel, "suggestion")){
                toggleSelection(holder, position);
            }else if(Objects.equals(listModel, "output")){
                toggleSelection(holder, position);
                showButton(holder,position);
            }
        });
        //按鈕點擊事件
        holder.btn_25.setOnClickListener(v -> {
            handleButtonClick(holder, holder.btn_25, "25");
            handleButtonClick(holder, item.getName(), "25");
        });
        holder.btn_50.setOnClickListener(v -> {
            handleButtonClick(holder, holder.btn_50, "50");
            handleButtonClick(holder, item.getName(), "50");
        });
        holder.btn_75.setOnClickListener(v -> {
            handleButtonClick(holder, holder.btn_75, "75");
            handleButtonClick(holder, item.getName(), "75");
        });
        holder.btn_100.setOnClickListener(v -> {
            handleButtonClick(holder, holder.btn_100, "100");
            handleButtonClick(holder, item.getName(), "100");
        });

    }

    private void handleButtonClick(MyViewHolder holder, String objectName, String number) {
        // 這裡記錄物件名稱與數字
        System.out.println("物件名稱: " + objectName + ", 數字: " + number);

        // 如果需要存到集合中
        buttonSelections.put(objectName, number);
        System.out.println(buttonSelections);
        // 其他操作
    }
    public static void outputSelection() {
        // 需要操作的物件列表
        List<MyItem> itemsToRemove = new ArrayList<>();

        for (int position : selectedItems) {
            MyItem item = itemList.get(position);
            String itemName = item.getName();

            if (buttonSelections.containsKey(itemName)) {
                // 取得選取的數值
                int value = Integer.parseInt(buttonSelections.get(itemName));

                // 计算新的百分比
                int newPercent = item.getPercent() - value;

                if (newPercent <= 0) {
                    // 如果新百分比小於等於 0，將該物件加入待刪除列表
                    itemsToRemove.add(item);
                    System.out.println("Item removed: " + item.getName());
                } else {
                    // 更新百分比
                    item.setPercent(newPercent);
                    System.out.println(item.getName());
                    System.out.println("Updated percent: " + newPercent);
                }
            }
        }

        // 删除所有百分比小於等於0的物件
        for (MyItem item : itemsToRemove) {
            itemList.remove(item);
            // 从 selectedItems 中移除被删除项的位置
            selectedItems.remove(Integer.valueOf(itemList.indexOf(item)));
        }

        // 清空按鈕選擇記錄
        buttonSelections.clear();

    }
    public static void updatePercent(MyViewHolder holder, int newPercent){
        holder.circularProgressBar.setProgress(newPercent);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textViewTime;
        ImageView imageView;
        ProgressBar circularProgressBar;
        boolean checked;
        Button btn_25;
        Button btn_50;
        Button btn_75;
        Button btn_100;
        LinearLayout button_container;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);
            imageView = itemView.findViewById(R.id.item_image);
            textViewTime = itemView.findViewById(R.id.item_time);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
            TextView suggestText = itemView.findViewById(R.id.suggest_text);
            checked = false;
            btn_25 = itemView.findViewById(R.id.btn_25);
            btn_50 = itemView.findViewById(R.id.btn_50);
            btn_75 = itemView.findViewById(R.id.btn_75);
            btn_100 = itemView.findViewById(R.id.btn_100);
            button_container = itemView.findViewById(R.id.button_container);
        }

    }

    // 切换选中状态
    private void toggleSelection(MyViewHolder holder, int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(Integer.valueOf(position));
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
            String text = "";
            for (Integer i : selectedItems) {
                if (i >= 0 && i < itemList.size()) {
                    MyItem item = itemList.get(i);
                    text += item.getName() + " , ";
                } else {
                    System.out.println("Invalid index: " + i);
                }
            }
            MainActivity.setSuggestText(SuggesstText+text);
        } else {
            selectedItems.add(position);
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#d3d3d3")));
            String text = "";
            for (Integer i : selectedItems) {
                if (i >= 0 && i < itemList.size()) {
                    MyItem item = itemList.get(i);
                    text += item.getName() + " , ";
                } else {
                    System.out.println("Invalid index: " + i);
                }
            }
            MainActivity.setSuggestText(SuggesstText+text);
        }
    }

    public static String getSelectedItems() {
        String text = "";
        for (Integer i : selectedItems) {
            if (i >= 0 && i < itemList.size()) {
                MyItem item = itemList.get(i);
                text += item.getName() + " , ";
            } else {
                System.out.println("Invalid index: " + i);
            }
        }
        return text;
    }
    public void clearSelectedItems(){
        selectedItems.clear();
    }

    // 确认操作（在外部调用这个方法来处理确认操作）
    public static void confirmSelection() {
        for (int position : selectedItems) {
            MyItem item = itemList.get(position);
            System.out.println("被选中item: " + item.getName());
            //this.holder.button_container.setVisibility(View.GONE);
        }
        selectedItems.clear();
    }

    public void showButton(MyViewHolder holder, int position) {
        if (Objects.equals(listModel, "output") && selectedItems.contains(position)) {
            holder.button_container.setVisibility(View.VISIBLE); // 顯示按鈕
        } else {
            holder.button_container.setVisibility(View.GONE); // 隱藏按鈕
        }
    }

    private void handleButtonClick(MyViewHolder holder, Button selectedButton, String value) {
        // 取消所有按鈕的選中狀態（恢復樣式）
        resetButtonStyles(holder);

        // 設定當前按鈕為選中狀態
        selectedButton.setBackgroundColor(Color.BLUE); // 或自定義選中背景
        selectedButton.setTextColor(Color.WHITE);

        // 處理選中邏輯，例如保存選中值
        System.out.println();
        System.out.println("選中值: " + value);
    }
    private void resetButtonStyles(MyViewHolder holder) {
        // 恢復按鈕的初始樣式
        holder.btn_25.setBackgroundColor(Color.LTGRAY); // 初始背景顏色
        holder.btn_25.setTextColor(Color.BLACK);

        holder.btn_50.setBackgroundColor(Color.LTGRAY);
        holder.btn_50.setTextColor(Color.BLACK);

        holder.btn_75.setBackgroundColor(Color.LTGRAY);
        holder.btn_75.setTextColor(Color.BLACK);

        holder.btn_100.setBackgroundColor(Color.LTGRAY);
        holder.btn_100.setTextColor(Color.BLACK);
    }
    public void resetAllStates() {
        selectedItems.clear(); // 清空選中的項目
        buttonSelections.clear(); // 清空按鈕選擇記錄
        notifyDataSetChanged(); // 通知列表重新繪製
    }
    public void resetButtonSelection() {
        buttonSelections.clear();  // 清除所有選中的按鈕記錄
        for (int i = 0; i < itemList.size(); i++) {
            MyItem item = itemList.get(i);
            notifyItemChanged(i);  // 通知每個項目重新繪製
        }
    }
}
