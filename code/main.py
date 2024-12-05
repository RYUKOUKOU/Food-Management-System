from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO
import sqlite3
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import secure_filename
import json
import Args
import os   

# 初始化 Flask 和 SocketIO
main = Flask(__name__)
socketio = SocketIO(main)

# Flask 根路由
@main.route('/')
def index():
    return render_template('index.html')

# 接收来自 Java 端的请求
@main.route('/api/update_message', methods=['POST'])
def update_message():

    data = request.form.get('data')  # 获取表单字段中的 JSON 数据
    if not data:
        return jsonify({"error": "No data provided"}), 400

    # 解析 JSON 数据
    data_dict = json.loads(data)
    room_id = data_dict.get('id')  # 获取 JSON 中的 'id'
    
    # 获取上传的文件
    file = request.files.get('file')
    if file:
        # 如果需要，可以保存文件或进一步处理
        filename = file.filename  # 获取文件名
        file_path = os.path.join(Args.path, filename)
        file.save(file_path)  # 保存文件到服务器
    else:
        return jsonify({"error": "No file provided"}), 400
    
    return jsonify("success"), 200


# 处理从客户端（Java）发送的消息
@socketio.on('return_message')
def return_message(id):
    socketio.emit('rehome', id)


if __name__ == '__main__':
    Args=Args.Args()
    socketio.run(main, host='127.0.0.1', port=8000,debug=True)
