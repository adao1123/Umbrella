package com.foo.umbrella.util;

import com.foo.umbrella.data.providers.WeatherApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adao1 on 9/13/2017.
 */

public class ApiManager {
    private static final String WEATHER_BASE_URL = "http://api.wunderground.com/";
    private static Retrofit retrofit;

    public static WeatherApiService createWeatherApiService(){
        return getRetrofit(WEATHER_BASE_URL).create(WeatherApiService.class);
    }

    /**
     * Builds and returns Retrofit object built with static baseUrl and GsonConverterFactory
     * @return
     */
    private static Retrofit getRetrofit(String baseUrl){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient()).build();
        }
        return retrofit;
    }

    /**
     * Returns HTTPLogging to make it much easier to follow and debug retrofit api calls made
     * @return
     */
    private static OkHttpClient getOkHttpClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
