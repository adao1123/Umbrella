package com.foo.umbrella.data.providers;

import com.foo.umbrella.BuildConfig;
import com.foo.umbrella.data.models.CurrentOrigin;
import com.foo.umbrella.data.models.WeatherOrigin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by adao1 on 9/13/2017.
 */

public interface WeatherApiService {

    //http://api.wunderground.com/api/4b4561a232f4c166/hourly10day/q/CA/95035.json
    @GET("api/"+ BuildConfig.API_KEY+"/hourly/q/CA/{zip}.json")
    Call<WeatherOrigin> getWeatherOrigin(@Path("zip") String zipCode);

    //http://api.wunderground.com/api/4b4561a232f4c166/conditions/q/CA/San_Francisco.json
    @GET("api/"+ BuildConfig.API_KEY + "/conditions/q/CA/{zip}.json")
    Call<CurrentOrigin> getCurrentWeather (@Path("zip") String zipCode);
}
