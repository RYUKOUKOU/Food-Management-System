package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化登录按钮
        loginButton = findViewById(R.id.loginButton); // 确保在XML中为按钮添加了ID

        // 设置点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里可以添加登录逻辑，比如跳转到登录页面
                //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });
    }
}