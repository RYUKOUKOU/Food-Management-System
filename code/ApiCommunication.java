import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.UUID;

public class ApiCommunication {

    public static void main(String[] args) {
        communicationApi("101", null, null);
}   
    @SuppressWarnings("unused")
    public static void communicationApi(String service_id,String message,File imageFile){
        // Flask 服务的 API 路由
        String apiUrl = "http://127.0.0.1:8000/api/update_message";
        
        // 请求体 JSON 数据
        String requestBodyJson = "{\"id\":" + service_id + ", \"message\":\"" + message + "\"}";
      
        // Boundary 用于分隔表单数据部分
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replaceAll("-", "");
        try {
            // 创建 HTTP 连接
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法和头部信息
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
                if(imageFile != null){
                    os.write(("--" + boundary + "\r\n").getBytes());
                    os.write("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"\r\n".getBytes());
                    os.write("Content-Type: image/jpeg\r\n\r\n".getBytes());
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                    os.write(imageBytes);
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
}