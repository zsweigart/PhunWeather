package com.zacharysweigart.phunweather.util;

import com.zacharysweigart.phunweather.model.WeatherStatus;

/**
 * This interface defines functions for any activity that will want to update weather information on
 * its screen
 *
 * @author Zachary Sweigart
 */
public interface PopulateWeatherUiInterface {

    public void populateUi();

    public void setWeatherStatus(WeatherStatus weatherStatus);
}
