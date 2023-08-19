package com.example.androidproject.Api.Prediction;

import com.example.androidproject.model.PredictionModel;
import com.example.androidproject.model.SearchModel;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface predictionApi {

    @POST("predict")
    Call<PredictionModel> getPrediction (@Body JsonObject body);//@Header("Authorization") String token
}
