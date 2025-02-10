import os
class Args:
    def __init__(self):
        self.path = os.path.dirname(os.path.abspath(__file__))
        self.model_dir = os.path.join(self.path, "models")
        self.model =['best.pt','last.pt','best8.pt']
        self.confidence_threshold = 0.1

    foodDate = {
        'lemon': 21,
        'potato': 30, 
        'banana': 7, 
        'broccoli': 3, 
        'orange': 21, 
        'carrot': 14,
        'Apple': 21,   
        'Banana': 7,  
        'Broccoli': 3,
        'Cabbage': 7, 
        'Carrot': 14, 
        'Cucumber': 7,
        'Grape': 7,   
        'Lemon': 21,  
        'Orange': 21, 
        'Potato': 30  
    }

#选择通信时的操作码
# upload_img. 上传图片并识别图片中的食物名称
# return_food. 确认食物名称并返回食物信息
# upload_login. 登录信息
# return_login. 返回登录信息
# upload_register. 注册信息
# return_register. 返回注册信息
# upload_fooduse. 食物使用情况

# upload_suggestion 建议
# return_suggestion 返回建议