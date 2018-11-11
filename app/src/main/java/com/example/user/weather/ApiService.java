package com.example.user.weather;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {
    static final String BASEURL ="http://api.openweathermap.org/data/2.5/";
    static final String APPKEY = "0de9e340d1d963cfa2636d025871f92c";
    static final String iconUrl = "http://openweathermap.org/img/w/";
    @GET("weather")
    Call<WeatherResult> getHourly (@Query("lat") double lat, @Query("lon") double lon,
                                @Query("appid") String appid );
}
