from asyncio import sleep
from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO,emit
import sqlite3
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import secure_filename
import json
import Args
import os   
import time
#import ImagePredict

# 初始化 Flask 和 SocketIO
main = Flask(__name__)
socketio = SocketIO(main)
global model

# Flask 根路由
@main.route('/')
def index():
    return render_template('index.html')

# 接收来自 Java 端的请求
@main.route('/api/update_message', methods=['POST'])
def update_message():
    data = request.form.get('data')
    if not data:
        return jsonify({"error": "No data provided"}), 400
    # 解析 JSON 数据
    data_dict = json.loads(data)
    service_id = data_dict.get('id')
    message = data_dict.get('message')
    #具体处理接收到的信息
    if service_id == 'update_img':
        file = request.files.get('file')
        if file:
            #json_data = json.dumps(ImagePredict.return_food_info(ImagePredict.predict(model,file)))
            json_data = "1"
            #return_message('return_food', json_data)
            return jsonify({"id": "return_food", "message": "2"}), 200
        else:
            return jsonify({"error": "No file provided"}), 400
    else:
        time.sleep(5)  # 修复 asyncio.sleep
        return_message("101","102")
          # 修复 emit 调用
    return jsonify({"id": "return_food", "message": "1"}), 200
        
    

# 处理从客户端（Java）发送的消息
@socketio.on('return_message')
def return_message(id, message):
    socketio.emit('return_message', {'id': "101", 'message': "102"}, namespace='/')



if __name__ == '__main__':
    Args=Args.Args()
    #model = ImagePredict.predict_init(Args,x=0)
    socketio.run(main, host='127.0.0.1', port=8000)