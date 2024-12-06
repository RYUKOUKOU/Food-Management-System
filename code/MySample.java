import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;

public class MySample {

    public static void main(String[] args) {
        // 启动一个新线程来保持与服务器的连接
        new Thread(() -> {
            try {
                Socket socket = IO.socket("http://127.0.0.1:8000");
                socket.on("return_message", args1 -> {
                    JSONObject message = (JSONObject) args1[0];
                    System.out.println("收到来自服务器的消息: " + message.getString("message"));
                });
                socket.connect();
                while (true) {
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
