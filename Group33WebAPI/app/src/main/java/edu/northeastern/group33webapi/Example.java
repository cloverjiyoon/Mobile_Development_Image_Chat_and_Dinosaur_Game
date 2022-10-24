package edu.northeastern.group33webapi;

import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("clouds")
    private Cloud cloud;

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

}
