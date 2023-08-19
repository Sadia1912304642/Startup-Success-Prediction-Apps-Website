# Import necessary libraries
import joblib
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.conf import settings
import numpy as np
import json
import sys
import os

# Load the model from the static folder
path_to_model = os.path.join(settings.STATIC_URL, 'model/')
print(path_to_model)
loaded_model = joblib.load(
    open('/Users/jiaulhaquesaboj/Desktop/CSE299/Startup/model/startup_model.pkl', 'rb'))

# Create your views here.


@api_view(['GET'])
def index(request):
    return_data = {
        "error_code": "0",
        "info": "success",
    }
    return Response(return_data)


@api_view(["POST"])
def predict_startup_status(request):
    try:
        # load the request data
        startup_json_info = request.data

        # Retrieve all the values from the json data
        startup_info = np.array(list(startup_json_info.values()))

        # Make prediction
        startup_status = loaded_model.predict([startup_info])

        # Model confidence score
        model_confidence_score = np.max(
            loaded_model.predict_proba([startup_info]))

        model_prediction = {
            'info': 'success',
            'Startup_status': startup_status[0],
            # 'model_confidence_proba': float("{:.2f}".format(model_confidence_score*100))
        }

    except ValueError as ve:
        model_prediction = {
            'error_code': '-1',
            "info": str(ve)
        }

    return Response(model_prediction)
