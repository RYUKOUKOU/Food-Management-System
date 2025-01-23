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
import java.util.List;
import java.util.Objects;

public class MyImageTextAdapter extends RecyclerView.Adapter<MyImageTextAdapter.MyViewHolder> {

    private static List<MyItem> itemList = null;
    private String listModel = MainActivity.listModel;
    private static List<Integer> selectedItems = new ArrayList<>();

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
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
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

    public void outputCheck(MyViewHolder holder){
        if (!holder.checked){
            holder.checked = true;
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#d3d3d3")));
            holder.button_container.setVisibility(View.VISIBLE);
        }else {
            holder.checked = false;
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
            holder.button_container.setVisibility(View.GONE);
        }
    }
}
