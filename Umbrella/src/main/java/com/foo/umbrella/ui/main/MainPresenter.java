package com.foo.umbrella.ui.main;

import android.support.annotation.Nullable;

import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;
import com.foo.umbrella.data.source.WeatherRepository;
import com.foo.umbrella.util.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adao1 on 9/13/2017.
 */

public class MainPresenter implements MainContract.Presenter {

    private final WeatherRepository mWeatherRepository;
    private MainContract.View mMainView;
    private List<Forecast> forecasts;
    private CurrentObservation currentObservation;
    private String zipCode;

    public MainPresenter() {
        mWeatherRepository = WeatherRepository.getINSTANCE();
    }

    @Override
    public void bindView(MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void unbind() {
        mMainView = null;
    }

    @Override
    public void start(@Nullable String zipCode) {
        if (zipCode == null){
            openZipcodeDialog();
            initialLoad(Constants.ZIP_DEFAULT);
        }
        else {
            initialLoad(zipCode);
        }
    }

    /**
     * This method will tell repository to make API call.
     * Since we want the app to reload list every time the app becomes the foreground app (onStart()),
     * there is no need to have null or empty checks to avoid too many calls and return a previously loaded list.
     * @param zipCode
     * @see WeatherRepository#makeCurrentApiCall(MainPresenter, String)
     * @see WeatherRepository#makeForcastApiCall(MainPresenter, String)
     */
    private void initialLoad(String zipCode){
        mWeatherRepository.makeForcastApiCall(this,zipCode);
        mWeatherRepository.makeCurrentApiCall(this,zipCode);
        this.zipCode = zipCode;
    }

    /**
     * This method will be called from repository to store CurrentObservation object in Presenter.
     * It calls methods in views to display passed data.
     * @param currentObservation
     * @see MainActivity#displayCityTitle(String)
     * @see MainActivity#displayCurrentForecast(CurrentObservation)
     */
    public void setCurrent(CurrentObservation currentObservation){
        this.currentObservation = currentObservation;
        if (mMainView != null){
            mMainView.displayCityTitle(currentObservation.getDisplay_location().getFull());
            mMainView.displayCurrentForecast(currentObservation);
        }
    }

    /**
     * This method will be called from repository to store data list in Presenter.
     * It calls methods in views to display passed data.
     * @param forecasts
     * @see MainActivity#displayWeather(List)
     * @see #getDividedForcastList(List)
     */
    public void setForecasts(List<Forecast> forecasts){
        this.forecasts = forecasts;
        if (mMainView != null){
            mMainView.displayWeather(getDividedForcastList(this.forecasts));
        }
    }

    /**
     * This method will tell view to open dialog.
     * @see MainActivity#displayZipCodeDialog()
     */
    private void openZipcodeDialog(){
        if (mMainView != null){
            mMainView.displayZipCodeDialog();
        }
    }

    /**
     * This method will divide return list into multiple lists separated by day.
     * This method will also set which Forecast object is the coldest in each day.
     * @param forecastList
     * @return List<List<Forecast>> dividedForecastList
     */
    private List<List<Forecast>> getDividedForcastList(List<Forecast> forecastList){
        List<List<Forecast>> dividedForecastList = new ArrayList<>();
        if (forecastList.size() == 0) return dividedForecastList;
        List<Forecast> currentDayForcastList = new ArrayList<>();
        Forecast initialForecast = forecastList.get(0);
        String day = initialForecast.getFCTTIME().getWeekday_name();
        Forecast hottestHour = initialForecast;
        Forecast coldestHour = initialForecast;
        int hottestTemp = Integer.MIN_VALUE;
        int coldestTemp = Integer.MAX_VALUE;
        initialForecast.setColdest(true);
        initialForecast.setHottest(true);
        for (Forecast forecast : forecastList){
            if (!forecast.getFCTTIME().getWeekday_name().equals(day)){
                dividedForecastList.add(currentDayForcastList);
                currentDayForcastList = new ArrayList<>();
                day = forecast.getFCTTIME().getWeekday_name();
                hottestTemp = Integer.MIN_VALUE;
                coldestTemp = Integer.MAX_VALUE;
                coldestHour = null;
                hottestHour = null;
            }
            if (Integer.parseInt(forecast.getTemp().getEnglish()) < coldestTemp){
                if (coldestHour != null) coldestHour.setColdest(false);
                coldestHour = forecast;
                coldestTemp = Integer.parseInt(forecast.getTemp().getEnglish());
                coldestHour.setColdest(true);
            }
            if (Integer.parseInt(forecast.getTemp().getEnglish()) > hottestTemp){
                if (hottestHour != null) hottestHour.setHottest(false);
                hottestHour = forecast;
                hottestTemp = Integer.parseInt(forecast.getTemp().getEnglish());
                hottestHour.setHottest(true);
            }
            currentDayForcastList.add(forecast);
        }
        return dividedForecastList;
    }
}
