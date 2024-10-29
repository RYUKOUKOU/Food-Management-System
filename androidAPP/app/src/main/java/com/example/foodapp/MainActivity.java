package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<MyItem> myData = new ArrayList<>();
        myData.add(new MyItem("base", R.drawable.login_background));
        MyImageTextAdapter myAdapter = new MyImageTextAdapter(myData, this);
        recyclerView.setAdapter(myAdapter);


        // 通知按钮
        ImageButton helpButton = findViewById(R.id.informButton);  // 通知按钮
        helpButton.setOnClickListener(new View.OnClickListener() {  // 通知按钮
            @Override
            public void onClick(View v) {  // 点击时调用
                //startActivity(new Intent(MainActivity.this, InformActivity.class));  // 启动informActivity
                myData.add(new MyItem("Item L", R.drawable.login_background));
            }
        });

        // 登录按钮
        ImageButton loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                myData.add(new MyItem("Item R", R.drawable.login_background));
            }
        });

        // 导航栏按钮
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_input) {
                    myData.add(new MyItem("Item 1", R.drawable.login_background));
                    return true;
                } else if (itemId == R.id.nav_output) {
                    myData.add(new MyItem("Item 2", R.drawable.login_background));
                    return true;
                } else if (itemId == R.id.nav_suggestion) {
                    myData.add(new MyItem("Item 3", R.drawable.login_background));
                    return true;
                }
                return false;
            }
        });
    }

    //通信部分函数
    public void communicationApi(){

    };

}
