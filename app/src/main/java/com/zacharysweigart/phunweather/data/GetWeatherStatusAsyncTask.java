package com.zacharysweigart.phunweather.data;

import android.content.Context;
import android.os.AsyncTask;

import com.zacharysweigart.phunweather.R;
import com.zacharysweigart.phunweather.model.WeatherStatus;
import com.zacharysweigart.phunweather.util.PopulateWeatherUiInterface;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This task will make the api calls to get the weather status
 *
 * @author Zachary Sweigart
 */
public class GetWeatherStatusAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private PopulateWeatherUiInterface ui;
    private String zipcode;
    private WeatherStatus weatherStatus;
    HttpClient httpclient;

    public GetWeatherStatusAsyncTask(Context context, PopulateWeatherUiInterface ui, String zipcode) {
        this.context = context;
        this.ui = ui;
        this.zipcode = zipcode;
        weatherStatus = new WeatherStatus();
        httpclient = new DefaultHttpClient();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String [] location = getLocation();
        getCurrentWeather(location);
        getForecast(location);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ui.setWeatherStatus(weatherStatus);
        ui.populateUi();
    }

    private String[] getLocation() {
        String [] location = new String [2];
        String url = context.getString(R.string.wunderground_api_call_start) + context.getString(R.string.wunderground_api_key)
                + "/" + context.getString(R.string.wunderground_geolookup) + "/q/" + zipcode + ".json";

        try {
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();

                JSONObject jsonObject = new JSONObject(out.toString());
                JSONObject locationJson = jsonObject.getJSONObject(context.getString(R.string.wunderground_geolookup_location));
                location[0] = locationJson.getString(context.getString(R.string.wunderground_geolookup_state));
                location[1] = locationJson.getString(context.getString(R.string.wunderground_geolookup_city));
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        //Default location to austin texas
        } catch(ClientProtocolException ex) {
            location[0] = "TX";
            location[1] = "Austin";
        } catch (IOException io) {
            location[0] = "TX";
            location[1] = "Austin";
        } catch (JSONException jsonex) {
            location[0] = "TX";
            location[1] = "Austin";
        }

        location[1] = location[1].replace(" ", "%20");

        return location;
    }

    private void getCurrentWeather(String[] location) {
        String url = context.getString(R.string.wunderground_api_call_start) + context.getString(R.string.wunderground_api_key)
                + "/" + context.getString(R.string.wunderground_conditions) + "/q/" + location[0] + "/"
                + location[1] + ".json";

        try {
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();

                JSONObject jsonObject = new JSONObject(out.toString());
                JSONObject currentObservation = jsonObject.getJSONObject(context.getString(R.string.wunderground_conditions_current_observation));
                weatherStatus.setZip(zipcode);
                weatherStatus.setLocation(currentObservation.getJSONObject(context.getString(R.string.wunderground_conditions_display_location))
                        .getString(context.getString(R.string.wunderground_conditions_display_location_string)));
                weatherStatus.setObservationTime(currentObservation.getLong(context.getString(R.string.wunderground_conditions_observation_time)));
                weatherStatus.setObservationTimeString(currentObservation.getString(context.getString(R.string.wunderground_conditions_observation_time_string)));
                weatherStatus.setCurrentTemp(currentObservation.getDouble(context.getString(R.string.wunderground_conditions_current_temp)));
                weatherStatus.setFeelsLikeTemp(currentObservation.getDouble(context.getString(R.string.wunderground_conditions_feels_like_temp)));
                weatherStatus.setHumidity(currentObservation.getString(context.getString(R.string.wunderground_conditions_humidity)));
                weatherStatus.setDewPoint(currentObservation.getDouble(context.getString(R.string.wunderground_conditions_dew_point)));
                weatherStatus.setPressure(currentObservation.getDouble(context.getString(R.string.wunderground_conditions_pressure)));
                weatherStatus.setWindSpeed(currentObservation.getDouble(context.getString(R.string.wunderground_conditions_wind_speed)));
                weatherStatus.setWindDirection(currentObservation.getString(context.getString(R.string.wunderground_conditions_wind_direction)));
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch(ClientProtocolException ex) {
            //Do nothing
        } catch (IOException io) {
            //Do nothing
        } catch (JSONException jsonex) {
            //Do nothing
        }
    }

    private void getForecast(String [] location) {
        String url = context.getString(R.string.wunderground_api_call_start) + context.getString(R.string.wunderground_api_key)
                + "/" + context.getString(R.string.wunderground_forecast) + "/q/" + location[0] + "/"
                + location[1] + ".json";

        try {
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();

                JSONObject jsonObject = new JSONObject(out.toString());
                JSONObject forecast = jsonObject.getJSONObject(context.getString(R.string.wunderground_forecast))
                        .getJSONObject(context.getString(R.string.wunderground_simple_forecast))
                        .getJSONArray(context.getString(R.string.wunderground_forecast_day)).getJSONObject(0);

                weatherStatus.setHighTemp(forecast.getJSONObject(context.getString(R.string.wunderground_forecast_high))
                        .getDouble(context.getString(R.string.wunderground_forecast_fahrenheit)));
                weatherStatus.setLowTemp(forecast.getJSONObject(context.getString(R.string.wunderground_forecast_low))
                        .getDouble(context.getString(R.string.wunderground_forecast_fahrenheit)));
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch(ClientProtocolException ex) {
            //Do nothing
        } catch (IOException io) {
            //Do nothing
        } catch (JSONException jsonex) {
            //Do nothing
        }
    }
}
