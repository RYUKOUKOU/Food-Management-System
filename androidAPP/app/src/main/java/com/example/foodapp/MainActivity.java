package com.example.foodapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        ImageButton informButton = findViewById(R.id.informButton);  // 通知按钮
        informButton.setOnClickListener(new View.OnClickListener() {  // 通知按钮
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
                String requestBody = ""; // 用于传递给 API 的数据

                if (itemId == R.id.nav_input) {
                    myData.add(new MyItem("Item 1", R.drawable.login_background));
                    return true;
                } else if (itemId == R.id.nav_output) {
                    myData.add(new MyItem("Item 2", R.drawable.login_background));
                    return true;
                } else if (itemId == R.id.nav_suggestion) { myData.add(new MyItem("Item 3", R.drawable.login_background));
                    return true;
                }
                return false;

            }
        });
    }

    // 通信部分函数
    public void communicationApi(String requestBody) {
        new Thread(() -> {
            String apiUrl = "http://10.0.2.2:8000/api/update_message"; // Flask 服务器地址
            HttpURLConnection connection = null;

            try {
                // 创建 URL 对象
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();

                // 设置请求方法为 POST
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                // 启用输出流，发送 POST 请求体
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.getBytes());
                    os.flush();
                }

                // 获取响应代码
                int responseCode = connection.getResponseCode();
                Log.d("API Response", "Response Code: " + responseCode);

                // 读取响应数据
                try (Scanner scanner = new Scanner(
                        responseCode >= 200 && responseCode < 300
                                ? connection.getInputStream()
                                : connection.getErrorStream())) {
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    if (responseCode >= 200 && responseCode < 300) {
                        String responseString = response.toString();
                        Log.d("API Response", "Response: " + responseString);

                        // 假设服务器返回 JSON 格式数据，解析并提取 operator
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String operator = jsonResponse.getString("operator");

                        // 根据 operator 调用不同的函数
                        handleOperator(operator);
                    } else {
                        Log.e("API Error", "Error Response: " + response);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
    //通信部分函数

    // 根据 operator 来调用不同的函数
    private void handleOperator(String operator) {
        switch (operator) {
            case "1":
                functionOne();
                break;
            case "2":
                functionTwo();
                break;
            case "3":
                functionThree();
                break;
            default:
                defaultFunction();
                break;
        }
    }

    // 示例函数
    private void functionOne() {
        Log.d("Function", "Function 1 executed");
        // 这里可以执行对应的逻辑
    }

    private void functionTwo() {
        Log.d("Function", "Function 2 executed");
        // 这里可以执行对应的逻辑
    }

    private void functionThree() {
        Log.d("Function", "Function 3 executed");
        // 这里可以执行对应的逻辑
    }

    private void defaultFunction() {
        Log.d("Function", "Default function executed");
        // 默认操作
    }
}
