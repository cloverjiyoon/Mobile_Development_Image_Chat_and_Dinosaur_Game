package edu.northeastern.group33webapi;

import com.google.gson.annotations.SerializedName;

public class Cloud {
    @SerializedName("all")
    String cloudlevel;

//    public String getDeg() {
//        return deg;
//    }
//
//    public void setDeg(String deg) {
//        this.deg = deg;
//    }
//
//    @SerializedName("deg")
//    String deg;

    public Cloud(String cloudlevel){
        this.cloudlevel = cloudlevel;
//        this.deg = deg;
    }

    public String getCloudlevel() {
        return cloudlevel;
    }

    public void setCloudlevel(String cloudlevel) {
        this.cloudlevel = cloudlevel;
    }


}
