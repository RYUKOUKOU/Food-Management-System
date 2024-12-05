import os
class Args:
    def __init__(self):
        self.path = os.path.dirname(os.path.abspath(__file__))
        self.model_dir = os.path.join(self.path, "models")
        self.model =['best.pt','last.pt']