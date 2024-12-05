from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO
import sqlite3
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import secure_filename

# 初始化 Flask 和 SocketIO
main = Flask(__name__)
socketio = SocketIO(main)

# 设置允许的扩展名
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

# 检查文件扩展名是否被允许
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# Flask 根路由
@main.route('/')
def index():
    return render_template('index.html')

# 接收来自 Java 端的请求
@main.route('/api/update_message', methods=['POST'])
def update_message():
    data = request.get_json()  # 假设客户端发送的是 JSON 格式数据
    room_id = 'room' + str(data.get('id', 0))  # 获取 ID，并构建房间名

    # 通过 SocketIO 向客户端发送消息
    socketio.emit(room_id, '今掃除してもよろしいでしょうか')

    return jsonify({"status": "success", "message": "Message sent to room " + room_id}), 200

# 上传图片接口（仅返回图片名）
@main.route('/api/upload_image', methods=['POST'])
def upload_image():
    if 'image' not in request.files:
        return jsonify({"status": "error", "message": "No image file provided"}), 400

    file = request.files['image']
    if file.filename == '':
        return jsonify({"status": "error", "message": "No selected file"}), 400

    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)  # 确保文件名安全
        # 不保存文件，只返回文件名
        return jsonify({"status": "success", "message": f"Image name: {filename}"}), 200

    return jsonify({"status": "error", "message": "File not allowed"}), 400

# 处理从客户端（Java）发送的消息
@socketio.on('return_message')
def return_message(id):
    socketio.emit('rehome', id)

# 用户注册
@main.route('/api/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    if not username or not password:
        return jsonify({"status": "error", "message": "Username and password are required"}), 400

    hashed_password = generate_password_hash(password)
    try:
        with sqlite3.connect("users.db") as conn:
            cursor = conn.cursor()
            cursor.execute("INSERT INTO users (username, password) VALUES (?, ?)", (username, hashed_password))
            conn.commit()
        return jsonify({"status": "success", "message": "User registered successfully"}), 201
    except sqlite3.IntegrityError:
        return jsonify({"status": "error", "message": "Username already exists"}), 409

# 用户登录
@main.route('/api/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    if not username or not password:
        return jsonify({"status": "error", "message": "Username and password are required"}), 400

    with sqlite3.connect("users.db") as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT password FROM users WHERE username = ?", (username,))
        user = cursor.fetchone()

        if user and check_password_hash(user[0], password):
            return jsonify({"status": "success", "message": "Login successful"}), 200
        else:
            return jsonify({"status": "error", "message": "Invalid credentials"}), 401

if __name__ == '__main__':
    init_db()  # 确保数据库已初始化
    socketio.run(main, host='127.0.0.1', port=8000)
