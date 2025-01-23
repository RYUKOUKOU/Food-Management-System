import os
import google.generativeai as genai


API_KEY = 'AIzaSyCyyLE3ydkkDlpwyfbE3UgvyfHDbxGIgeU'
genai.configure(api_key=API_KEY)
model = genai.GenerativeModel("gemini-pro")

chat = model.start_chat(history=[
    {
        "role": "user",
        "parts": ["語尾はござるで答えてください。"]
    },
    {
        "role": "model",
        "parts": ["承知しました。"]
    },
])



def chat_with_bot(foodlist):
    food_str = ", ".join(foodlist)
    user_message = f"以下の食材: {food_str} を使ってレシピを教えてください、ござるか？使用した具体的な材料と調理方法をお知らせください。"
    response = chat.send_message(user_message)
    return response.text
