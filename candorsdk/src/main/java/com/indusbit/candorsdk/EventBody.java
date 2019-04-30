package com.indusbit.candorsdk;

import com.google.gson.annotations.SerializedName;

public class EventBody {
    @SerializedName("event")
    Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
