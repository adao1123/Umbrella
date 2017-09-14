package com.foo.umbrella.data.source;

import android.util.Log;

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

    /**
     * This method returns the single static instance of the repository.
     * @return WeatherRepository INSTANCE
     */
    public static WeatherRepository getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new WeatherRepository();
        }
        return INSTANCE;
    }

    /**
     * This method makes (enqueues) the current api call on a separate thread.
     * It is called from the presenter and will tell the presenter when it is returned.
     * @param presenter
     * @param zipCode
     * @see Call#enqueue(Callback)
     */
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

    /**
     * This method makes (enqueues) the hourly api call on a separate thread.
     * It is called from the presenter and will tell the presenter when it is returned.
     * @param presenter
     * @param zipCode
     * @see Call#enqueue(Callback)
     */
    public void makeForcastApiCall(final MainPresenter presenter, String zipCode){
        getForecastApiCall(zipCode).enqueue(new Callback<WeatherOrigin>() {
            @Override
            public void onResponse(Call<WeatherOrigin> call, Response<WeatherOrigin> response) {
                presenter.setForecasts(Arrays.asList(response.body().getForecastArray()));
                Log.i("AAAAAAAAAAAAAAAAAAAAA", "onResponse: ");
            }

            @Override
            public void onFailure(Call<WeatherOrigin> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    /**
     * This method will get Retrofit Call for Hourly weather from ApiManager.
     *
     * @param zipCode
     * @see ApiManager#createWeatherApiService()#getForecastApiCall
     * @return Retrofit Call
     */
    private Call<WeatherOrigin> getForecastApiCall(String zipCode){
        return ApiManager.createWeatherApiService().getWeatherOrigin(zipCode);
    }

    /**
     * This method will get Retrofit Call for Current weather from ApiManager.
     *
     * @param zipCode
     * @see ApiManager#createWeatherApiService()#getCurrentApiCall
     * @return Retrofit Call
     */
    private Call<CurrentOrigin> getCurrentApiCall(String zipCode){
        return ApiManager.createWeatherApiService().getCurrentWeather(zipCode);
    }

}
