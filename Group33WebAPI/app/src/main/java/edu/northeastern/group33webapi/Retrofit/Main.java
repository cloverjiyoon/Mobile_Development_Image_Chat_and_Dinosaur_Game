package edu.northeastern.group33webapi.Retrofit;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    String temp;

    @SerializedName("humidity")
    String humidity;

    @SerializedName("pressure")
    String pressure;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}
