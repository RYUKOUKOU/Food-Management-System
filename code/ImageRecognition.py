from ultralytics import YOLO
import os
import Args

def predict_init(args,x=0):
    model = YOLO( os.path.join(args.model_dir, args.model[x]))
    return model

def predict(model, img):
    res = model.predict(source=img)
    return res[0]