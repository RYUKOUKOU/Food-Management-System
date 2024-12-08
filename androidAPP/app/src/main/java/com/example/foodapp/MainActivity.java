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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static String Server_url = null;
    public static String API_URL = "http://"+ Server_url + "/api/update_message";
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
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
                    new API("101", null, null).execute();

                    return true;
                } else if (itemId == R.id.nav_suggestion) {
                    myData.add(new MyItem("Item 3", R.drawable.login_background));
                    return true;
                }
                return false;

            }
        });
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

