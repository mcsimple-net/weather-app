package com.improve.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=*************=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);

    @GET("weather?appid=**************&units=metric")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String city);

}
