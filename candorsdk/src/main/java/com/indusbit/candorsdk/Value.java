package com.indusbit.candorsdk;

import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("key")
    String key;
    @SerializedName("type")
    String type;
    @SerializedName("value")
    String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
