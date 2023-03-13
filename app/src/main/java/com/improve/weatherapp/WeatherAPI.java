package com.improve.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=ccaf1b3c554ff9521df4fa82b0a324b2&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);

    @GET("weather?appid=ccaf1b3c554ff9521df4fa82b0a324b2&units=metric")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String city);

}
