from ultralytics import YOLO
import os
import Args

def predict_init(args,x=0):
    model = YOLO( os.path.join(args.model_dir, args.model[x]))
    return model

def predict(model, img):
    res = model.predict(source=img)
    return res[0]

def return_food_info(result,args=Args):
  boxes = result.boxes
  names = result.names
  confidence_threshold = args.confidence_threshold
  object_counts = {}
  for box in boxes:
      confidence = box.conf[0]
      class_id = int(box.cls[0])
      if confidence >= confidence_threshold:
          class_name = names[class_id]
          if class_name in object_counts:
              object_counts[class_name] += 1
          else:
              object_counts[class_name] = 1
  return object_counts