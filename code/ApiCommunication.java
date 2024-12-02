public void communicationApi(String requestBody) {
    new Thread(() -> {
        String apiUrl = "http://10.0.2.2:8000/api/update_message"; // Flask 服务器地址
        HttpURLConnection connection = null;

        try {
            // 创建 URL 对象
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为 POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // 启用输出流，发送 POST 请求体
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes());
                os.flush();
            }

            // 获取响应代码
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

                // 解析响应数据
                if (responseCode >= 200 && responseCode < 300) {
                    String responseString = response.toString();
                    System.out.println("Response: " + responseString);

                    // 假设服务器返回 JSON 格式数据，解析并提取 operator
                    JSONObject jsonResponse = new JSONObject(responseString);
                    String operator = jsonResponse.getString("operator");

                    // 根据 operator 调用不同的函数
                    handleOperator(operator);
                } else {
                    System.err.println("Error Response: " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }).start();
}

// 根据 operator 来调用不同的函数
private void handleOperator(String operator) {
    switch (operator) {
        case "1":
            // 调用函数 1
            functionOne();
            break;
        case "2":
            // 调用函数 2
            functionTwo();
            break;
        case "3":
            // 调用函数 3
            functionThree();
            break;
        default:
            // 默认操作
            defaultFunction();
            break;
    }
}

// 示例函数
private void functionOne() {
    System.out.println("Function 1 executed");
    // 这里可以执行对应的逻辑
}

private void functionTwo() {
    System.out.println("Function 2 executed");
    // 这里可以执行对应的逻辑
}

private void functionThree() {
    System.out.println("Function 3 executed");
    // 这里可以执行对应的逻辑
}

private void defaultFunction() {
    System.out.println("Default function executed");
    // 默认操作
}
