import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.URI;

public class MySample {

    public static void main(String[] args) {
        // 假设有图片上传功能（只传回图片文件）
        String imagePath = "C:\\Users\\bb\\Pictures\\Saved Pictures\\img_4d081dca447804f6dcb5588544007601135806.jpg";  // 替换成实际图片路径
        String message = "Test message with image";

        // 调用 API 上传图片及消息
        MySample sample = new MySample();
        sample.communicationApi(message, imagePath);
    }

    public void communicationApi(String requestBody, String imagePath) {
        new Thread(() -> {
            String apiUrl = "http://127.0.0.1:8000/api/update_message"; // 正确的 API URL
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                URI uri = URI.create(apiUrl);
                HttpPost httpPost = new HttpPost(uri);

                // 创建 MultipartEntityBuilder 来构建表单
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addPart("message", new StringBody(requestBody, org.apache.http.entity.ContentType.TEXT_PLAIN));
                
                // 图片文件传递
                File file = new File(imagePath);
                builder.addPart("image", new FileBody(file));

                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);

                // 执行请求
                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println("API Response: " + responseString);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    httpClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
