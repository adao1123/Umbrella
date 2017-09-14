package com.foo.umbrella.ui.main;

import android.util.Log;

import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;
import com.foo.umbrella.data.source.WeatherRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adao1 on 9/13/2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
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
    public void start(String zipCode) {
        initialLoad(zipCode);
    }

    @Override
    public void updateList(String zipCode) {
        Log.i(TAG, "updateList: " + zipCode);
        reload(zipCode);
    }

    private void initialLoad(String zipCode){
        if (forecasts == null || forecasts.isEmpty() || !this.zipCode.equals(zipCode)){
            mWeatherRepository.makeForcastApiCall(this,zipCode);
        } else {
            setForecasts(forecasts);
        }
        if (currentObservation == null || !this.zipCode.equals(zipCode)){
            mWeatherRepository.makeCurrentApiCall(this,zipCode);
        } else {
            setCurrent(currentObservation);
        }
        this.zipCode = zipCode;
    }

    private void reload(String zipCode){
        mWeatherRepository.makeForcastApiCall(this,zipCode);
        mWeatherRepository.makeCurrentApiCall(this,zipCode);
    }

    public void setCurrent(CurrentObservation currentObservation){
        this.currentObservation = currentObservation;
        if (mMainView != null){
            mMainView.displayCityTitle(currentObservation.getDisplay_location().getFull());
            mMainView.displayCurrentForecast(currentObservation);
        }
    }

    public void setForecasts(List<Forecast> forecasts){
        this.forecasts = forecasts;
        if (mMainView != null){
            mMainView.displayWeather(getDividedForcastList(this.forecasts));
        }
    }

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
