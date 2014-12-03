package com.zacharysweigart.phunweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zacharysweigart.phunweather.R;
import com.zacharysweigart.phunweather.data.GetWeatherStatusAsyncTask;
import com.zacharysweigart.phunweather.model.WeatherStatus;
import com.zacharysweigart.phunweather.util.PopulateWeatherUiInterface;

public class WeatherDetailsActivity extends Activity implements PopulateWeatherUiInterface {
    private SharedPreferences sharedPreferences;
    private WeatherStatus weatherStatus;
    private String zipcode;

    // UI Elements
    private ProgressBar progressBar;
    private TextView locationTextView;
    private TextView timeTextView;
    private TextView currentTempTextView;
    private TextView forecastTextView;
    private TextView feelsLikeTempTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView dewpointTextView;
    private TextView windTextView;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        instantiateUiElements();

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.zipcode))) {
            zipcode = intent.getStringExtra(getString(R.string.zipcode));
        } else {
            Toast.makeText(this, "The weather status could not be found", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(!getCachedWeather()) {
            new GetWeatherStatusAsyncTask(this, this, zipcode).execute();
        } else {
            populateUi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Gson gson = new Gson();
        String jsonString = gson.toJson(weatherStatus);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(getString(R.string.cached_weather)+zipcode, jsonString);
        sharedPrefEditor.commit();
    }

    private void instantiateUiElements() {
        progressBar = (ProgressBar) findViewById(R.id.progress);
        locationTextView = (TextView) findViewById(R.id.location);
        timeTextView = (TextView) findViewById(R.id.observation_time);
        currentTempTextView = (TextView) findViewById(R.id.current_temp);
        forecastTextView = (TextView) findViewById(R.id.forecast_temp);
        feelsLikeTempTextView = (TextView) findViewById(R.id.feels_like_temp);
        humidityTextView = (TextView) findViewById(R.id.humidity);
        pressureTextView = (TextView) findViewById(R.id.pressure);
        dewpointTextView = (TextView) findViewById(R.id.dewpoint);
        windTextView = (TextView) findViewById(R.id.wind);
        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new GetWeatherStatusAsyncTask(WeatherDetailsActivity.this, WeatherDetailsActivity.this, zipcode).execute();
            }
        });
    }

    private boolean getCachedWeather() {
        String cachedWeather = sharedPreferences.getString(getString(R.string.cached_weather)+zipcode, "");
        if(!cachedWeather.equals("")) {
            Gson gson = new Gson();
            WeatherStatus cachedWeatherStatus = gson.fromJson(cachedWeather, WeatherStatus.class);

            // If the cached data is more than 15 minutes old disregard it
            if(cachedWeatherStatus == null || (System.currentTimeMillis()/1000 - cachedWeatherStatus.getObservationTime()) > (15 * 60 * 1000)) {
                return false;
            }

            // Otherwise return true that we can use the cached status
            weatherStatus = cachedWeatherStatus;
            return true;
        }
        return false;
    }

    @Override
    public void populateUi() {
        locationTextView.setText(weatherStatus.getLocation());

        timeTextView.setText(weatherStatus.getObservationTimeString());
        currentTempTextView.setText(weatherStatus.getCurrentTemp() + "° F");
        forecastTextView.setText(weatherStatus.getLowTemp() + "° F/" + weatherStatus.getHighTemp() + "° F");
        feelsLikeTempTextView.setText(weatherStatus.getFeelsLikeTemp() + "° F");
        humidityTextView.setText(weatherStatus.getHumidity());
        pressureTextView.setText(weatherStatus.getPressure() + " In.");
        dewpointTextView.setText(weatherStatus.getDewPoint() + "° F");
        windTextView.setText(weatherStatus.getWindSpeed() + " " + weatherStatus.getWindDirection());
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }
}
