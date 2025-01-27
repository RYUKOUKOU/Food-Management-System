package com.example.foodapp;

import static com.example.foodapp.MainActivity.API_URL;
import android.os.AsyncTask;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class API extends AsyncTask<Void, Void, String> {
    private String service_id;
    private String message;
    private byte[] imageFile;

    public API(String service_id, String message, byte[] imageFile) {
        this.service_id = service_id;
        this.message = message;
        this.imageFile = imageFile;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String requestBodyJson = "{\"id\":\"" + service_id + "\", \"message\":\"" + message + "\"}";
        System.out.println(requestBodyJson);
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
                if (imageFile != null) {
                    os.write(("--" + boundary + "\r\n").getBytes());
                    os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"\r\n").getBytes());
                    os.write("Content-Type: image/png\r\n\r\n".getBytes());

                    // 直接写入字节数组
                    os.write(imageFile);
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

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject jsonResponse = new JSONObject(result);
            String service_id = jsonResponse.getString("id");
            String message = jsonResponse.getString("message");

            System.out.println("Service ID: " + service_id + "message:" + message);

            if (service_id.equals("return_food")) MainActivity.getFoodList(message);
            if (service_id.equals("return_suggestion")) MainActivity.returnSuggest(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}