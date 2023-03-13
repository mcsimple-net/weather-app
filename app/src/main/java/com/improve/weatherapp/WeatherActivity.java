package com.improve.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView cityWeather, temperatureWeather, pressureWeather, windWeather, humidityWeather
            , condition, maxTemperatureWeather, minTemperatureWeather;
    private ImageView imageViewWeather;
    private Button search;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityWeather = findViewById(R.id.textViewCityWeather);
        temperatureWeather = findViewById(R.id.textViewTempWeather);
        pressureWeather = findViewById(R.id.textViewPressureWeather);
        windWeather = findViewById(R.id.textViewWindWeather);
        humidityWeather = findViewById(R.id.textViewHumidityWeather);
        condition = findViewById(R.id.textViewWeatherConditionWeather);
        maxTemperatureWeather = findViewById(R.id.textViewTempMaxWeather);
        minTemperatureWeather = findViewById(R.id.textViewTempMinWeather);
        imageViewWeather = findViewById(R.id.imageViewWeather);
        search = findViewById(R.id.search);
        editText = findViewById(R.id.editTextCityName);

        search.setOnClickListener(view -> {
            String cityName = editText.getText().toString();

            getWeatherData(cityName);

            editText.setText("");
        });

    }

    public void getWeatherData(String city) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(city);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if (response.isSuccessful()) {
                    OpenWeatherMap openWeatherMap = response.body();

                    cityWeather.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    temperatureWeather.setText(String.valueOf(response.body().getMain().getTemp()) + " °C");
                    pressureWeather.setText(String.valueOf(response.body().getMain().getPressure()) + " hPa");
                    windWeather.setText(String.valueOf(response.body().getWind().getSpeed()) + " m/s");
                    humidityWeather.setText(String.valueOf(response.body().getMain().getHumidity()) + " %");
                    condition.setText(response.body().getWeather().get(0).getDescription());
                    maxTemperatureWeather.setText(String.valueOf(response.body().getMain().getTempMax()) + " °C");
                    minTemperatureWeather.setText(String.valueOf(response.body().getMain().getTempMin()) + " °C");

                    String icon = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(imageViewWeather);
                }
                else {
                    System.out.println(response.code());
                    Toast.makeText(WeatherActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}