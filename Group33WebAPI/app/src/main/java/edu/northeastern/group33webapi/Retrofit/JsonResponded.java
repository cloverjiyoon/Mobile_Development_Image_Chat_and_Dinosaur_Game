package edu.northeastern.group33webapi.Retrofit;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JsonResponded {

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private ArrayList<Weather> weather;

    public Weather getWeather() {
        return weather.get(0);
    }

//    public void setWeather(Weather weather) {
//        this.weather = weather;
//    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}


