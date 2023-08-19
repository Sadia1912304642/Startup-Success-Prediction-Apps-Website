package com.example.androidproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PredictionModel {

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("Startup_status")
    @Expose
    private int Startup_status;

    public String getInfo() {
        return info;
    }

    public int getStartup_status() {
        return Startup_status;
    }
}
