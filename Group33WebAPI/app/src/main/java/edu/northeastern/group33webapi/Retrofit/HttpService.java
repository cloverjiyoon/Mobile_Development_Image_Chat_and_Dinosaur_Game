package edu.northeastern.group33webapi.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpService {

    @GET("/data/2.5/weather?appid=27e1a53a3bdda61a1925402df0f93673&units=metric")
    Call<JsonResponded> getWeatherData(@Query("q") String name);
}
