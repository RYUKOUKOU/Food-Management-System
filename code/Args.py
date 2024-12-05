import os
class Args:
    def __init__(self):
        self.path = os.path.dirname(os.path.abspath(__file__))
        self.model_dir = os.path.join(self.path, "models")
        self.model =['best.pt','last.pt']

#选择通信时的操作码
# update_img. 上传图片并识别图片中的食物名称
# return_food. 确认食物名称并返回食物信息
# update_login. 登录信息
# return_login. 返回登录信息
# update_register. 注册信息
# return_register. 返回注册信息
# update_fooduse. 食物使用情况

# update_suggestion 建议
# return_suggestion 返回建议