package com.example.user.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResult {
    @SerializedName("weather")
    @Expose
    private List<Weather> weathers;

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    @SerializedName("main")
    @Expose
    private Main mains;

    public Main getMains() {
        return mains;
    }

    public void setMains(Main mains) {
        this.mains = mains;
    }

    @SerializedName("wind")
    @Expose
    private Wind winds;

    public Wind getWinds() {
        return winds;
    }

    public void setWinds(Wind winds) {
        this.winds = winds;
    }

    @SerializedName("clouds")
    @Expose
    private Clouds cloud;

    public Clouds getCloud() {
        return cloud;
    }

    public void setCloud(Clouds cloud) {
        this.cloud = cloud;
    }

    @SerializedName("sys")
    @Expose
    private Sys sys;

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
