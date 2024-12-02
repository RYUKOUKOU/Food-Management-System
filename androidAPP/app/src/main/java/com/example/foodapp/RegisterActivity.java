package com.example.foodapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        EditText usernameEditText = findViewById(R.id.registerUsername);
        EditText passwordEditText = findViewById(R.id.registerPassword);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                registerUser(username, password);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String username, String password) {
        new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL("http://10.0.2.2:8000/api/register");
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

                if (responseCode == 201) {
                    runOnUiThread(() -> Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show());
                } else if (responseCode == 409) {
                    runOnUiThread(() -> Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Registration Failed: Code " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error occurred during registration", e);
                runOnUiThread(() -> Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
