package com.indusbit.candorsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Variant {
    @SerializedName("key")
    String key;
    @SerializedName("values")
    List<Value> values;

    public final static String DEFAULT = "default";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}
