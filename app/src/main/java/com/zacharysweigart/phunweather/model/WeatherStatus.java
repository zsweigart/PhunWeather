package com.zacharysweigart.phunweather.model;

/**
 * This class represents a weather status as is displayed on the details activity
 *
 * @author Zachary Sweigart
 */
public class WeatherStatus {
    String location;
    String zip;
    long observationTime;
    double currentTemp;
    double feelsLikeTemp;
    double lowTemp;
    double highTemp;
    String humidity;
    double dewPoint;
    double pressure;
    double windSpeed;
    String windDirection;
    String observationTimeString;

    public WeatherStatus() {
        location = "";
        zip = "";
        observationTime = 0;
        currentTemp = 0;
        feelsLikeTemp = 0;
        lowTemp = 0;
        highTemp = 0;
        humidity = "0%";
        dewPoint = 0;
        pressure = 0;
        windSpeed = 0;
        windDirection = "";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public long getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(long observationTime) {
        this.observationTime = observationTime;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(double feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getObservationTimeString() {
        return observationTimeString;
    }

    public void setObservationTimeString(String observationTimeString) {
        this.observationTimeString = observationTimeString;
    }
}
