package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 通知按钮
        ImageButton informButton = findViewById(R.id.informButton);  // 通知按钮
        informButton.setOnClickListener(new View.OnClickListener() {  // 通知按钮
            @Override
            public void onClick(View v) {  // 点击时调用
                //startActivity(new Intent(MainActivity.this, InformActivity.class));  // 启动informActivity
            }
        });

        ImageButton homeButton = findViewById(R.id.action_home);  // 主页面按钮
        homeButton.setOnClickListener(new View.OnClickListener() {  // 主页面按钮点击监听器
            @Override
            public void onClick(View v) {  // 点击时调用
                System.out.println("点击");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);  // 创建意图跳转到 MainActivity
                startActivity(intent);  // 启动 MainActivity
                finish();  // 结束当前 Activity
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        EditText usernameEditText = findViewById(R.id.loginUsername);
        EditText passwordEditText = findViewById(R.id.loginPassword);
        EditText serverAddressEditText = findViewById(R.id.serverAddress);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String serverAddress = serverAddressEditText.getText().toString().trim();

            if (!serverAddress.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                loginUser( username, password,serverAddress); // 将服务器地址传递给 loginUser 方法
            }  else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            // Navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loginUser( String username, String password,String serverAddress) {
        new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL("http://10.0.2.2:8000/api/login");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.getBytes());
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                if (responseCode == 200) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // 登录成功后传递 serverAddress 到 MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("SERVER_ADDRESS", serverAddress);  // 将服务器地址传递给 MainActivity
                        startActivity(intent);
                        finish(); // 关闭当前活动
                    });
                } else if (responseCode == 401) {
                    runOnUiThread(() -> Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Login Failed: Code " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error during login", e);
                runOnUiThread(() -> Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
