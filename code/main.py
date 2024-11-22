from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO

main = Flask(__name__)
socketio = SocketIO(main)

# Flask 根路由
@main.route('/')
def index():
    return render_template('index.html')

# 接收来自 Java 端的请求
@main.route('/api/update_message', methods=['POST'])
def update_message():
    # 这里我们从请求体中获取 ID
    data = request.get_json()  # 假设客户端发送的是 JSON 格式数据
    room_id = 'room' + str(data.get('id', 0))  # 获取 ID，并构建房间名
    
    # 通过 SocketIO 向客户端发送消息
    socketio.emit(room_id, '今掃除してもよろしいでしょうか')
    
    return jsonify({"status": "success", "message": "Message sent to room " + room_id}), 200

# 处理从客户端（Java）发送的消息
@socketio.on('return_message')
def return_message(id):
    socketio.emit('rehome', id)

if __name__ == '__main__':
    main.run(host='127.0.0.1', port=8000)
