package com.malykhinv.chuck.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("value")
    @Expose
    private final List<Value> value = null;

    public List<Value> getValue() {
        return value;
    }
}