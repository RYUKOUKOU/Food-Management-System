import os
class Args:
    path = os.path.dirname(os.path.abspath(__file__))
    model_dir = os.path.join(path, "models")
    model =['best.pt','last.pt']