package com.foo.umbrella.data.source;

import com.foo.umbrella.data.models.CurrentOrigin;
import com.foo.umbrella.data.models.WeatherOrigin;
import com.foo.umbrella.ui.main.MainContract;
import com.foo.umbrella.ui.main.MainPresenter;
import com.foo.umbrella.util.ApiManager;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by adao1 on 9/13/2017.
 */

public class WeatherRepository {
    private static WeatherRepository INSTANCE = null;

    public static WeatherRepository getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new WeatherRepository();
        }
        return INSTANCE;
    }

    public void makeCurrentApiCall(final MainPresenter presenter, String zipCode){
        getCurrentApiCall(zipCode).enqueue(new Callback<CurrentOrigin>() {
            @Override
            public void onResponse(Call<CurrentOrigin> call, Response<CurrentOrigin> response) {
                presenter.setCurrent(response.body().getCurrent_observation());
            }

            @Override
            public void onFailure(Call<CurrentOrigin> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void makeForcastApiCall(final MainPresenter presenter, String zipCode){
        getForecastApiCall(zipCode).enqueue(new Callback<WeatherOrigin>() {
            @Override
            public void onResponse(Call<WeatherOrigin> call, Response<WeatherOrigin> response) {
                presenter.setForecasts(Arrays.asList(response.body().getForecastArray()));
            }

            @Override
            public void onFailure(Call<WeatherOrigin> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private Call<WeatherOrigin> getForecastApiCall(String zipCode){
        return ApiManager.createWeatherApiService().getWeatherOrigin(zipCode);
    }

    private Call<CurrentOrigin> getCurrentApiCall(String zipCode){
        return ApiManager.createWeatherApiService().getCurrentWeather(zipCode);
    }

}
