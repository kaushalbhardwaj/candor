package com.indusbit.candorsdk;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

public class Event {

    @SerializedName("name")
    String name;
    String properties;
    long id;

    Event(String name, String properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    private void setId(long id) {
        this.id = id;
    }
}
