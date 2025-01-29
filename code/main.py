from asyncio import sleep
from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO,emit
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import secure_filename
import json
import Args
import os   
import ImagePredict
import cv2
import useAndAi


# 初始化 Flask 和 SocketIO
main = Flask(__name__)
socketio = SocketIO(main)
global model

# Flask 根路由
@main.route('/')
def index():
    return render_template('index.html')

# 接收来自 Java 端的请求
@main.route('/', methods=['POST'])
def update_message():
    data = request.form.get('data')
    if not data:
        return jsonify({"error": "No data provided"}), 400
    # 解析 JSON 数据
    data_dict = json.loads(data)
    service_id = data_dict.get('id')
    message = data_dict.get('message')
    #具体处理接收到的信息
    # 上传图片
    if service_id == 'upload_img':
        file = request.files.get('file')
        if file:
            file.save(os.path.join(Args.path, "image.png"))
            img = cv2.imread(os.path.join(Args.path, "image.png"))
            json_data = (ImagePredict.return_food_info(ImagePredict.predict(model,img),Args))
            #演示使用
            #ImagePredict.img_show(Args,img,ImagePredict.predict(model,img))
            if not json_data:
                return jsonify({"error": "No data received"}), 400  # 回傳 400 Bad Request
            for key in json_data:
                json_data[key] = Args.foodDate[key]
            os.remove(os.path.join(Args.path, "image.png"))
            return jsonify({"id": "return_food", "message": json_data}), 200
        else:
            return jsonify({"id": "error", "message": "No file provided"}), 400
        
        # 上传建议
    elif service_id == 'upload_suggestion':
        result = useAndAi.chat_with_bot(message)
        return jsonify({"id": "return_suggestion", "message": result}), 200

    return jsonify({"id": "error", "message": "wrong service id"}), 200
        
# 暂时弃用
@socketio.on('return_message')
def return_message(id, message):
    socketio.emit('return_message', {'id':id, 'message': message}, namespace='/')



if __name__ == '__main__':
    Args=Args.Args()
    model = ImagePredict.predict_init(Args,x=0)
    socketio.run(main, host='0.0.0.0', port=8000)
