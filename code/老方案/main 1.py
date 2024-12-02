from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/update_message', methods=['POST'])
def update_message():
    # 获取 JSON 数据
    data = request.get_json()
    if data:
        # 提取 action 和 message
        action = data.get('action', 'No action specified')
        message = data.get('message', 'No message provided')

        # 打印到服务器端控制台
        print(f"Action: {action}, Message: {message}")

        # 返回响应
        return jsonify({"status": "success", "action": action, "message": message}), 200
    else:
        print("No JSON data received.")
        return jsonify({"status": "failed", "reason": "No JSON data"}), 400

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8000)
