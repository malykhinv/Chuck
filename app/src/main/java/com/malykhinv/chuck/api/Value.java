package com.malykhinv.chuck.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("joke")
    @Expose
    private String joke;

    public String getJoke() {
        return joke;
    }

}
