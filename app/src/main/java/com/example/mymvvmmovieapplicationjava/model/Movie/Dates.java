package com.example.mymvvmmovieapplicationjava.model.Movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Dates {

    @SerializedName("maximum")
    @Expose
    private Date maximum;

    @SerializedName("minimum")
    @Expose
    private Date minimum;

    public Date getMaximum() {
        return maximum;
    }

    public void setMaximum(Date maximum) {
        this.maximum = maximum;
    }

    public Date getMinimum() {
        return minimum;
    }

    public void setMinimum(Date minimum) {
        this.minimum = minimum;
    }

}
