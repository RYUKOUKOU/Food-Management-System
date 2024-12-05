import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiCommunication {
    public static void main(String[] args) {
        // API URL (Flask 端的 API 路由)
        String apiUrl = "http://127.0.0.1:8000/api/update_message";

        // 请求体（这里我们发送一个 ID）
        String requestBody = "{\"id\": 101}";

        try {
            // 创建 URL 对象
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");

            // 启用输出流，发送 POST 请求体
            connection.setDoOutput(true);s
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes());
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
