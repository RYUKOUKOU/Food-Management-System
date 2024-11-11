from flask import Flask,render_template,request,url_for
from flask_socketio import SocketIO

main = Flask(__name__)
socketio = SocketIO(main)

@main.route('/')
def index():
    return render_template('index.html')

@socketio.on('update_message')
def handle_message(id):
    roomid = 'room'+str(id)
    socketio.emit(roomid, '今掃除してもよろしいでしょうか')
@socketio.on('return_message')
def return_message(id):
    socketio.emit('rehome', id)


    
if __name__ == '__main__':
    main.run(host='127.0.0.1', port=8000)