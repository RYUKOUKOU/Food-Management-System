from flask import Flask, request, jsonify
import os

# 初始化 Flask 应用
app = Flask(__name__)

# 配置上传文件的存储路径
UPLOAD_FOLDER = 'uploads/'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# 上传图片接口，保存图片并返回图片名称
@app.route('/api/update_message', methods=['POST'])
def update_message():
    message = request.form.get('message')  # 获取消息文本
    image = request.files.get('image')  # 获取图片文件

    if image:
        image_name = image.filename  # 获取图片的文件名
        image.save(os.path.join(app.config['UPLOAD_FOLDER'], image_name))  # 保存图片到服务器

        # 返回成功响应
        return jsonify({"status": "success", "message": f"Received image: {image_name}"}), 200
    else:
        return jsonify({"status": "error", "message": "No image provided"}), 400

if __name__ == '__main__':
    app.run(debug=True)
