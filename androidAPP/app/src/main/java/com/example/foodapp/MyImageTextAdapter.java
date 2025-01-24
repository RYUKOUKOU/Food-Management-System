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
    protected static Map<String, String> buttonSelection = new HashMap<>();
    public MyImageTextAdapter(List<MyItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyItem item = itemList.get(position);

        holder.textView.setText(item.getName());
        holder.textViewTime.setText(item.getShelfLife());
        holder.imageView.setImageResource(item.getImageResId());
        holder.circularProgressBar.setProgress(item.getPercent());

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
                showButton(holder);
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
        buttonSelection.put(objectName, number);
        System.out.println(buttonSelection);
        // 其他操作
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
        } else {
            selectedItems.add(position);
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#d3d3d3")));
        }
    }

    public List<Integer> getSelectedItems() {
        return selectedItems;
    }
    public void clearSelectedItems(){
        selectedItems.clear();
    }

    // 确认操作（在外部调用这个方法来处理确认操作）
    public static void confirmSelection() {
        for (int position : selectedItems) {
            MyItem item = itemList.get(position);
            System.out.println("被选中item: " + item.getName());
        }
        selectedItems.clear();
    }

    public void showButton(MyViewHolder holder){
        if (!holder.checked){
            holder.checked = true;
            holder.button_container.setVisibility(View.VISIBLE);
        }else {
            holder.checked = false;
            holder.button_container.setVisibility(View.GONE);
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


}
