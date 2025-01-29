package com.example.foodapp;

import static com.example.foodapp.MainActivity.API_URL;
import static com.example.foodapp.MainActivity.Server_url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        // 通知按钮
        ImageButton homeButton = findViewById(R.id.action_home);  // 获取 homeButton
        if (homeButton != null) {
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            System.out.println("找不到按钮");
            Log.e("LoginActivity", "homeButton is null!");
        }

    }

    private void loginUser( String username, String password,String serverAddress) {
    Server_url = serverAddress;
    API_URL = "http://"+ Server_url + ":8000";
    System.out.println(API_URL);
    new API("101", null, null).execute();
    }
}
