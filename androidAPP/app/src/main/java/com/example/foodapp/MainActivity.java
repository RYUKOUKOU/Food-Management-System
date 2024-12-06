package com.example.foodapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity {
    private static final String SOCKET_URL = "http://127.0.0.1:8000";
    private static final String API_URL = SOCKET_URL + "/api/update_message";
    private Socket mSocket;
    private static final String SERVER_URL = "http://10.0.2.2:8000";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

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
                    communicationApi("101", null, null);

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
                    checkPermissions();
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
    private void startSocketCommunication() {
        new Thread(() -> {
            try {
                // 创建 Socket 连接
                Socket socket = IO.socket(SOCKET_URL);

                // 监听服务器返回的消息
                socket.on("return_message", args -> {
                    JSONObject message = (JSONObject) args[0];
                    try {
                        System.out.println("收到来自服务器的消息: " + message.getString("message"));
                        List<MyItem> myData = new ArrayList<>();
                            myData.add(new MyItem("102", R.drawable.login_background));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });

                // 连接服务器
                socket.connect();
                System.out.println("Socket 已连接到服务器: " + SOCKET_URL);

                // 保持连接，防止线程退出
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void communicationApi(String service_id, String message, File imageFile) {
        String requestBodyJson = "{\"id\":\"" + service_id + "\", \"message\":\"" + (message != null ? message : "") + "\"}";
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replaceAll("-", "");
        try {
            // 创建 HTTP 连接
            URL url = new URL("http://10.0.2.2:8000/api/update_message");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            // 写入请求体数据
            try (OutputStream os = connection.getOutputStream()) {
                // 写入 JSON 数据部分
                os.write(("--" + boundary + "\r\n").getBytes());
                os.write("Content-Disposition: form-data; name=\"data\"\r\n".getBytes());
                os.write("Content-Type: application/json\r\n\r\n".getBytes());
                os.write(requestBodyJson.getBytes());
                os.write("\r\n".getBytes());

                // 写入图片文件部分
                if (imageFile != null && imageFile.exists()) {
                    os.write(("--" + boundary + "\r\n").getBytes());
                    os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + imageFile.getName() + "\"\r\n").getBytes());
                    os.write("Content-Type: image/jpeg\r\n\r\n".getBytes());

                    // 使用 FileInputStream 读取文件
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    os.write("\r\n".getBytes());
                }

                // 写入结束边界
                os.write(("--" + boundary + "--\r\n").getBytes());
                os.flush();
            }

            // 获取响应代码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应内容
            try (Scanner scanner = new Scanner(
                    responseCode >= 200 && responseCode < 300
                            ? connection.getInputStream()
                            : connection.getErrorStream())) {
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                if (responseCode >= 200 && responseCode < 300) {
                    System.out.println("Response: " + response);
                } else {
                    System.err.println("Error Response: " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    // 相机部分函数
    public void checkPermissions() {
        // 检查是否有相机权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);
        }
        // 检查是否有写入外部存储的权限（适配 Android 10 及以上版本）
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            // 如果权限已经被授予，启动拍照功能
            dispatchTakePictureIntent();
        }
    }

    // 处理权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean cameraPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean storagePermissionGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermissionGranted && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || storagePermissionGranted)) {
                // 权限授予，启动拍照功能
                dispatchTakePictureIntent();
            } else {
                // 权限被拒绝，提示用户
                Toast.makeText(this, "权限被拒绝，无法拍照", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 启动相机拍照意图
    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    // 处理拍照后的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 获取返回的图片并设置到ImageView
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
        }
    }
}

