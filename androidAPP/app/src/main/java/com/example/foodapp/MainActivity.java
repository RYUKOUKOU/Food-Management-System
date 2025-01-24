package com.example.foodapp;

import static com.example.foodapp.MyImageTextAdapter.buttonSelection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public static String Server_url = "10.0.2.2";
    public static String API_URL = "http://"+ Server_url + ":8000/api/upload_message";
    private static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath;
    private static ArrayList<MyItem> myData = null;
    @SuppressLint("StaticFieldLeak")
    private static MyImageTextAdapter myAdapter;
    public  static String listModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 设置 RecyclerView
        if (myData == null) {
            myData = new ArrayList<>();
            myData.add(new MyItem("Potato", 10));
            myData.add(new MyItem("Cucumber", 7));
            myData.add(new MyItem("Cabbage", 7));
            myData.add(new MyItem("Broccoli", 5));
            myData.add(new MyItem("Grape", 3));
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyImageTextAdapter(myData);
        recyclerView.setAdapter(myAdapter);


        // 通知按钮
        ImageButton informButton = findViewById(R.id.informButton);
        informButton.setOnClickListener(v -> {  // 点击时调用
            System.out.println("通知");

        });

        // 登录按钮
        ImageButton loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            //startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // 导航栏按钮
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.nav_input) {
                checkPermissions();
                return true;
            } else if (itemId == R.id.nav_output) {
                if (!Objects.equals(listModel, "output")) {
                    listModel = "output";
                    TextView suggestText = findViewById(R.id.suggest_text);
                    suggestText.setText("output mode");
                    popOverlayOn();
                }else {
                    popOverlayOff();
                    listModel = null;
                    myAdapter.clearSelectedItems();
                    myAdapter.notifyDataSetChanged();
                }
                new API("101", null, null).execute();

                return true;
            } else if (itemId == R.id.nav_suggestion) {
                if (!Objects.equals(listModel, "suggestion")){
                    listModel = "suggestion";
                    popOverlayOn();

                }else {
                    popOverlayOff();
                    listModel = null;
                    myAdapter.clearSelectedItems();
                    myAdapter.notifyDataSetChanged();
                }
            } else return false;

            return false;
        });
    }

    // 相机部分函数
    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            launchCamera();
        }
    }
    // 启动相机拍照意图
    @SuppressLint("QueryPermissionsNeeded")
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 确认有应用能处理相机 Intent
        File photoFile;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            System.out.println("无法创建图片文件");
            return;
        }
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.example.android.foodappfileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 处理拍照后的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false; // 不仅仅获取图片的大小，实际解码图片
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // 使用JPEG格式并保持高质量（100）
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                new API("upload_img",null,byteArray).execute();
                System.out.println("照片上传");
            } else {
                System.out.println("无法加载照片");
            }
        } else {
            System.out.println("拍照失败或取消");
        }
    }

    public static void getFoodList(String foodList) throws JSONException {
        JSONObject jsonObject = new JSONObject(foodList);
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            System.out.println("Key: " + key + ", Value: " + jsonObject.getInt(key));

            myData.add(new MyItem(key, 1));
            myAdapter.notifyItemInserted(myData.size() - 1);
        }
    }
    public static void uploadSuggesst(){}
    public static void returnSuggesst(){}
    public void popOverlayOn(){
        View popupOverlay = findViewById(R.id.popup_overlay);
        popupOverlay.setVisibility(View.VISIBLE);
        Button confirm = findViewById(R.id.popup_confirm);
        if (Objects.equals(listModel, "output")) {
            confirm.setOnClickListener(v -> {
                if (buttonSelection != null){
                    for(Map.Entry<String, String> entry : buttonSelection.entrySet()){

                    }
                }
                System.out.println("輸出确认");
            });
        }
        confirm.setOnClickListener(v -> {
            System.out.println("确认");
        });
        Button restart = findViewById(R.id.popup_restart);
        restart.setOnClickListener(v -> {
            System.out.println("重置");
        });
    }
    public void popOverlayOff(){
        View popupOverlay = findViewById(R.id.popup_overlay);
        popupOverlay.setVisibility(View.GONE);
        popupOverlay.findViewById(R.id.popup_confirm).setOnClickListener(null);
        popupOverlay.findViewById(R.id.popup_restart).setOnClickListener(null);
    }
}

