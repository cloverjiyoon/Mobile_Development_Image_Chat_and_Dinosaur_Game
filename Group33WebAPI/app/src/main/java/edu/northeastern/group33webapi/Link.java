package edu.northeastern.group33webapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import edu.northeastern.group33webapi.Retrofit.Main;

public class Link {
    @SerializedName("list")
    private List<Example> list;

    public List<Example> getList() {
        return list;
    }

    public void setList(List<Example> list) {
        this.list = list;
    }
}

