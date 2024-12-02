import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ApiCommunication {

    // 发送 POST 请求的方法
    public static void sendApiRequest(String requestBody) {
        String apiUrl = "http://10.0.2.2:8000/api/update_message"; // API 端点 URL

        HttpURLConnection connection = null;
        OutputStream os = null;
        BufferedReader reader = null;

        try {
            // 创建 URL 对象
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为 POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");  // 可选：如果服务器期望此头部

            // 启用输入和输出流
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 将请求体写入输出流
            os = connection.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();

            // 获取服务器返回的响应代码
            int responseCode = connection.getResponseCode();
            System.out.println("响应代码: " + responseCode);

            // 读取响应
            InputStreamReader isr;
            if (responseCode >= 200 && responseCode < 300) {
                // 如果是成功响应
                isr = new InputStreamReader(connection.getInputStream());
            } else {
                // 如果是错误响应
                isr = new InputStreamReader(connection.getErrorStream());
            }

            reader = new BufferedReader(isr);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // 打印响应或错误信息
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("响应: " + response);
            } else {
                System.err.println("错误响应: " + response);
            }

        } catch (Exception e) {
            // 发生异常时，打印堆栈跟踪信息
            e.printStackTrace();
        } finally {
            // 关闭流和连接
            try {
                if (os != null) os.close();
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // main 方法，程序入口
    public static void main(String[] args) {
        // 模拟调用 sendApiRequest 方法
        String requestBody = "{\"id\": 101}";
        sendApiRequest(requestBody);  // 调用 API 请求方法
    }
}
