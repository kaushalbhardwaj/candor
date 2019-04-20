package com.indusbit.candorsdk;

import com.google.gson.annotations.SerializedName;

public class Experiment {
    @SerializedName("key")
    String key;
    @SerializedName("variant")
    Variant variant;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }
}