package com.foo.umbrella.ui.main;

import android.support.annotation.Nullable;

import com.foo.umbrella.base.BasePresenter;
import com.foo.umbrella.base.BaseView;
import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;

import java.util.List;


/**
 * Created by adao1 on 9/13/2017.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {
        void displayWeather(List<List<Forecast>> forecasts);
        void displayCityTitle(String cityTitle);
        void displayCurrentForecast(CurrentObservation currentObservation);
        void displayZipCodeDialog();
    }

    public interface Presenter extends BasePresenter {
        void bindView(MainContract.View view);
        void unbind();
        void start(@Nullable String zipCode);
    }
}
