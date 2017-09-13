package com.foo.umbrella.data.source;

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

}
