package com.indusbit.candorsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Experiments {
    @SerializedName("experiments")
    List<Experiment> experiments;

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(List<Experiment> experiments) {
        this.experiments = experiments;
    }
}
