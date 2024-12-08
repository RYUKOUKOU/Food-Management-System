package com.example.foodapp;

import static com.example.foodapp.MainActivity.API_URL;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class API extends AsyncTask<Void, Void, String> {
    private String service_id;
    private String message;
    private File imageFile;

    public API(String service_id, String message, File imageFile) {
        this.service_id = service_id;
        this.message = message;
        this.imageFile = imageFile;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String requestBodyJson = "{\"id\":" + service_id + ", \"message\":\"" + message + "\"}";
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replaceAll("-", "");
        // 在这里执行你的网络请求代码
        String response = null;
        try {
            // 创建 HTTP 连接
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            // 写入请求体数据 (JSON 数据和图片)
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
                StringBuilder responseBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    responseBuilder.append(scanner.nextLine());
                }
                response = responseBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 在这里处理请求结果，比如更新 UI 等
        System.out.println("网络请求结果: " + result);
    }
}
