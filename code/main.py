from flask import Flask, request, jsonify


# Initialize the Flask app
app = Flask(__name__)

# Define your route
@app.route('/api/update_message', methods=['POST'])
def update_message():
    # Get JSON data from the request
    data = request.get_json()
    
    if data:
        action = data.get('action', 'No action specified')
        message = data.get('message', 'No message provided')

        # Print the action and message for debugging
        print(f"Action: {action}, Message: {message}")

        # Return a response with the action and message
        return jsonify({"status": "success", "action": action, "message": message}), 200
    else:
        print("No JSON data received.")
        return jsonify({"status": "failed", "reason": "No JSON data"}), 400

# Run the app
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8000)
