package com.foo.umbrella.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.foo.umbrella.R;
import com.foo.umbrella.base.UmbrellaApp;
import com.foo.umbrella.data.models.CurrentOrigin;
import com.foo.umbrella.data.models.WeatherOrigin;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View{

  private static final String TAG = MainActivity.class.getSimpleName();
  private MainContract.Presenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mPresenter = ((UmbrellaApp)getApplication()).getMainPresenter();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresenter.bindView(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mPresenter.unbind();
    if (!isChangingConfigurations()) {
      ((UmbrellaApp)getApplication()).releaseMainPresenter();
    }
  }

  @Override
  public void displayWeather(List<WeatherOrigin.Forecast> forecasts) {
    Log.i(TAG, "displayWeather: " + forecasts.size());
  }

  @Override
  public void displayCityTitle(String cityTitle) {
    Log.i(TAG, "displayCityTitle: " + cityTitle);
  }

  @Override
  public void displayCurrentForecast(CurrentOrigin.CurrentObservation currentObservation) {
    Log.i(TAG, "displayCurrentForecast: " + currentObservation.getWeather());
  }
}
