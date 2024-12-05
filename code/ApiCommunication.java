import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.UUID;

public class ApiCommunication {
    public static void main(String[] args) {
        // API URL (Flask 端的 API 路由)
        String apiUrl = "http://127.0.0.1:8000/api/update_message";

        // 请求体 JSON 数据
        String requestBodyJson = "{\"id\": 101 ,\"message\": \"Hello, World!\"}";

        // 图片文件路径
        String imagePath = "D:\\github\\Food-Management-System\\code\\ApiCommunication.java";

        try {
            // 调用 API
            String response = sendPostRequest(apiUrl, requestBodyJson, imagePath);
            System.out.println("Server Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 POST 请求
     * @param apiUrl API 的 URL
     * @param jsonData 要发送的 JSON 数据
     * @param imagePath 要发送的图片文件路径
     * @return 服务器返回的响应
     * @throws IOException 如果发生 I/O 错误
     */
    public static String sendPostRequest(String apiUrl, String jsonData, String imagePath) throws IOException {
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replaceAll("-", "");

        // 创建连接
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            // 写入 JSON 数据部分
            writeFormData(os, "data", "application/json", jsonData.getBytes(), boundary);

            // 写入图片文件部分
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                writeFormFile(os, "file", "image.jpg", "image/jpeg", imageBytes, boundary);
            }

            // 写入结束边界
            os.write(("--" + boundary + "--\r\n").getBytes());
            os.flush();
        }

        // 获取响应
        int responseCode = connection.getResponseCode();
        try (Scanner scanner = new Scanner(
                responseCode >= 200 && responseCode < 300
                        ? connection.getInputStream()
                        : connection.getErrorStream())) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            if (responseCode >= 200 && responseCode < 300) {
                return response.toString();
            } else {
                throw new IOException("Error Response: " + response.toString());
            }
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 写入表单数据部分
     * @param os 输出流
     * @param name 表单项的名称
     * @param contentType 内容类型
     * @param data 数据字节
     * @param boundary 分隔符
     * @throws IOException 如果发生 I/O 错误
     */
    private static void writeFormData(OutputStream os, String name, String contentType, byte[] data, String boundary) throws IOException {
        os.write(("--" + boundary + "\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n").getBytes());
        os.write(("Content-Type: " + contentType + "\r\n\r\n").getBytes());
        os.write(data);
        os.write("\r\n".getBytes());
    }

    /**
     * 写入表单文件部分
     * @param os 输出流
     * @param name 表单项的名称
     * @param fileName 文件名
     * @param contentType 内容类型
     * @param fileData 文件数据字节
     * @param boundary 分隔符
     * @throws IOException 如果发生 I/O 错误
     */
    private static void writeFormFile(OutputStream os, String name, String fileName, String contentType, byte[] fileData, String boundary) throws IOException {
        os.write(("--" + boundary + "\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        os.write(("Content-Type: " + contentType + "\r\n\r\n").getBytes());
        os.write(fileData);
        os.write("\r\n".getBytes());
    }
}
