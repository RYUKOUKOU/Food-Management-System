import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.UUID;
import java.util.Scanner;

public class MySample {
    public static void main(String[] args) {
        // API URL (Flask 端的 API 路由)
        String apiUrl = "http://127.0.0.1:8000/api/update_message";
        
        // 请求体（我们将发送一个 ID 和图片文件）
        String requestBodyJson = "{\"id\": 101 ,\"message\": \"Hello, World!\"}";

        // 图片文件路径
        String imagePath = "C:/Users/User/Documents/GitHub/Food-Management-System/homepage/20241127103532.png";
        
        // Boundary string to separate the parts in the request
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replaceAll("-", "");
        
        try {
            // 创建 URL 对象
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法
            connection.setRequestMethod("POST");
            
            // 设置请求头
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);  // Enable output stream for sending data
            
            try (OutputStream os = connection.getOutputStream()) {
                // 写入 JSON 数据部分
                os.write(("--" + boundary + "\r\n").getBytes());
                os.write("Content-Disposition: form-data; name=\"data\"\r\n".getBytes());
                os.write("Content-Type: application/json\r\n\r\n".getBytes());
                os.write(requestBodyJson.getBytes());
                os.write("\r\n".getBytes());
                
                // 写入图片文件部分
                os.write(("--" + boundary + "\r\n").getBytes());
                os.write("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"\r\n".getBytes());
                os.write("Content-Type: image/jpeg\r\n\r\n".getBytes());
                
                // 读取图片并写入输出流
                File imageFile = new File(imagePath);
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                os.write(imageBytes);
                os.write("\r\n".getBytes());
                
                // 结束边界
                os.write(("--" + boundary + "--\r\n").getBytes());
                os.flush();
            }
            
            // 检查响应代码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

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
                    System.out.println("Response: " + response);
                } else {
                    System.err.println("Error Response: " + response);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
