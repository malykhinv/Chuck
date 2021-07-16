package com.malykhinv.chuck.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IcndbApi {
    @GET("/jokes/random/{number}")
    Call<ApiResponse> getJokes(@Path("number") int countOfJokes);
}
