from ultralytics import YOLO
import os
import Args

def predict_init(args,x=0):
    model = YOLO( os.path.join(args.model_dir, args.model[x]))
    return model

def predict(model, img):
    res = model.predict(source=img)
    return res[0]

def return_food_info(res):
  class_ids = res.boxes.cls
  num = len(class_ids)
  class_names =res[0].names
  class_id = int(class_ids[0].item())
  class_name = class_names[class_id]
  return class_name,num