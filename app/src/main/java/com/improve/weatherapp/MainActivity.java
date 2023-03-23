package com.improve.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView city, temperature, pressure, wind, humidity, weatherCondition, maxTemperature, minTemperature;
    private ImageView imageView;
    private FloatingActionButton fab;
    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.textViewCity);
        temperature = findViewById(R.id.textViewTemp);
        pressure = findViewById(R.id.textViewPressure);
        wind = findViewById(R.id.textViewWind);
        humidity = findViewById(R.id.textViewHumidity);
        weatherCondition = findViewById(R.id.textViewWeatherCondition);
        maxTemperature = findViewById(R.id.textViewTempMax);
        minTemperature = findViewById(R.id.textViewTempMin);
        imageView = findViewById(R.id.imageView);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);


        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                Log.e("lat : ", String.valueOf(lat));
                Log.e("lon : ", String.valueOf(lon));
                getWeatherData(lat, lon);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }
        };

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && permissions.length > 0 &&ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    public void getWeatherData(double lat, double lon) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat,lon);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                city.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                temperature.setText(response.body().getMain().getTemp() + " °C");
                pressure.setText(response.body().getMain().getPressure() + " hPa");
                wind.setText(response.body().getWind().getSpeed() + " m/s");
                humidity.setText(response.body().getMain().getHumidity() + " %");
                weatherCondition.setText(response.body().getWeather().get(0).getDescription());
                maxTemperature.setText(response.body().getMain().getTempMax() + " °C");
                minTemperature.setText(response.body().getMain().getTempMin() + " °C");

                String icon = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(imageView);



            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}