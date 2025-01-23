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
import java.util.List;
import java.util.Objects;

public class MyImageTextAdapter extends RecyclerView.Adapter<MyImageTextAdapter.MyViewHolder> {

    private final List<MyItem> itemList;
    private String listModel = MainActivity.listModel;

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

        // 处理点击事件
        holder.itemView.setOnClickListener(v -> {
            listModel = MainActivity.listModel;
            System.out.println(listModel);
            if (Objects.equals(listModel, "suggestion")){
                check(holder);
            }else if(Objects.equals(listModel, "output")){
                outputCheck(holder);
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

    public void check(MyViewHolder holder){
        if (!holder.checked){
            holder.checked = true;
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#d3d3d3")));
        }else {
            holder.checked = false;
            holder.itemView.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
        }
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
