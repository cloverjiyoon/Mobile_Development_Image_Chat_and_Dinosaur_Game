package edu.northeastern.group33webapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExampleList {
    @SerializedName("list")
    List<Example> examples;

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }


}
