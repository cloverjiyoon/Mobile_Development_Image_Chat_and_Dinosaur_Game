package edu.northeastern.group33webapi.Retrofit;

import android.util.Base64;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public native static String getApi();

    private static Retrofit retrofit = null;

    public static String APIKEY = "https://api.openweathermap.org" ;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIKEY)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}