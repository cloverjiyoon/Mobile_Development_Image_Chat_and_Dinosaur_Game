package edu.northeastern.group33webapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public native static String getApi();

    private static Retrofit retrofit = null;

    public static String APIKEY = "https://api.openweathermap.org" ;

    public static ApiInterface getRetrofitClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIKEY)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiInterface.class);
    }
}
