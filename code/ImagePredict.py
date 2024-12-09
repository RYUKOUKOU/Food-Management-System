from ultralytics import YOLO
import os
import cv2

def predict_init(args,x=0):
    model = YOLO( os.path.join(args.model_dir, args.model[x]))
    return model

def predict(model, img):
    res = model.predict(source=img)
    return res[0]

def return_food_info(result,args):
  boxes = result.boxes
  names = result.names
  #confidence_threshold = 0.1
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

def img_show(args,image, *got):
    for results in got:
      if not results.boxes:
          print("No boxes detected.")
          cv2.imwrite(os.path.join(args.path, 'output.jpg'),image)
          return image

      boxes = results.boxes.xyxy
      scores = results.boxes.conf
      class_ids = results.boxes.cls
      class_names = results[0].names

      for i, box in enumerate(boxes):
          class_id = int(class_ids[i].item())

          x1, y1, x2, y2 = box.tolist()
          x1, y1, x2, y2 = map(int, [x1, y1, x2, y2])
          confidence = scores[i].item()
          class_name = class_names[class_id]
          color = (0, 0, 255)
          cv2.rectangle(image, (x1, y1), (x2, y2), color, 3)
          label = f'{class_name} {confidence:.2f}'
          cv2.putText(image, label, (x1, y1 - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, color, 2)
    cv2.imwrite(os.path.join(args.path, 'output.png'),image)
    return image