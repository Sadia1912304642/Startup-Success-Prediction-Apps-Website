
from django.urls import path
from startup_api import views

urlpatterns = [
    path('', views.index),
    path('predict', views.predict_startup_status),
]
