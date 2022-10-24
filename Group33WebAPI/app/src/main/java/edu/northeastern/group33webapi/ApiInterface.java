package edu.northeastern.group33webapi;

import java.util.List;

import edu.northeastern.group33webapi.Retrofit.JsonResponded;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/data/2.5/forecast?appid=27e1a53a3bdda61a1925402df0f93673&units=metric")
    Call<ExampleList> getListData(@Query("q") String name);
}
