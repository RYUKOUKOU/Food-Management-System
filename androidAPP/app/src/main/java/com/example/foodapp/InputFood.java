package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputFood {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static String currentPhotoPath;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    // 构造函数
    public InputFood(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        InputFood.context = context;
    }

    // 启动相机
    @SuppressLint("QueryPermissionsNeeded")
    public static void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // 创建图片文件
            } catch (IOException ex) {
                Log.e("Camera", "Error creating image file", ex);
            }

            // 如果文件创建成功，则启动相机
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.your package.file provider", photoFile); // 使用 FileProvider 获取 URI
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // 设置拍照后保存图片的位置
                ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_TAKE_PHOTO); // 启动相机
            }
        }
    }

    // 创建用于保存照片的文件
    private static File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_";
        File storageDir = context.getExternalFilesDir(null); // 获取外部存储目录
        File image = File.createTempFile(
                imageFileName,  /* 文件名前缀 */
                ".jpg",         /* 文件扩展名 */
                storageDir      /* 存储位置 */
        );

        currentPhotoPath = image.getAbsolutePath(); // 获取文件路径
        return image;
    }

    // 获取图片路径
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    // 处理相机返回结果的函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {
            // 读取并显示拍摄的照片
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath); // 读取图片文件
            // imageView.setImageBitmap(bitmap); // 可以显示到 ImageView 中
            // 处理拍照后的图片
        }
    }
}

