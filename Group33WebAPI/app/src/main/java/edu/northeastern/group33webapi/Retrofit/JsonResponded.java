package edu.northeastern.group33webapi.Retrofit;

import com.google.gson.annotations.SerializedName;

public class JsonResponded {

    @SerializedName("main")
    private Main main;


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
