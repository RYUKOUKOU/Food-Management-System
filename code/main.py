from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO
import sqlite3
from werkzeug.security import generate_password_hash, check_password_hash

<<<<<<< HEAD

# Initialize the Flask app
app = Flask(__name__)
=======
# 初始化 Flask 和 SocketIO
main = Flask(__name__)
socketio = SocketIO(main)
>>>>>>> 6eeec87fa610e4726dbdb3e1536f57a1b914aae0

# 数据库初始化
def init_db():
    with sqlite3.connect("users.db") as conn:
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            )
        ''')
        conn.commit()

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
