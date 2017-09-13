package com.foo.umbrella.ui.main;

import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;
import com.foo.umbrella.data.source.WeatherRepository;

import java.util.List;


/**
 * Created by adao1 on 9/13/2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private final WeatherRepository mWeatherRepository;
    private MainContract.View mMainView;
    private List<Forecast> forecasts;
    private CurrentObservation currentObservation;

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
    public void start() {
        initialLoad();
    }

    private void initialLoad(){
        if (forecasts == null || forecasts.isEmpty()){
            mWeatherRepository.makeForcastApiCall(this,"95035");
        } else {
            setForecasts(forecasts);
        }
        if (currentObservation == null){
            mWeatherRepository.makeCurrentApiCall(this,"95035");
        } else {
            setCurrent(currentObservation);
        }
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
            mMainView.displayWeather(this.forecasts);
        }
    }


}
