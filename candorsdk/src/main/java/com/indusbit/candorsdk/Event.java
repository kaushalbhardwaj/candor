package com.indusbit.candorsdk;

import org.json.JSONObject;

public class Event {
    String name;
    JSONObject properties;

    Event(String name, JSONObject properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }
}
