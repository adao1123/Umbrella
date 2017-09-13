package com.foo.umbrella.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.foo.umbrella.R;
import com.foo.umbrella.adapters.DailyForecastAdapter;
import com.foo.umbrella.base.UmbrellaApp;
import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View{

  private static final String TAG = MainActivity.class.getSimpleName();
  private MainContract.Presenter mPresenter;
  private List<List<Forecast>> forecasts;
  private DailyForecastAdapter forecastAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mPresenter = ((UmbrellaApp)getApplication()).getMainPresenter();
    initRecyclerView();
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
  public void displayWeather(List<List<Forecast>> forecasts) {
    Log.i(TAG, "displayWeather: " + forecasts.size());
    updateList(forecasts);
  }

  @Override
  public void displayCityTitle(String cityTitle) {
    Log.i(TAG, "displayCityTitle: " + cityTitle);
  }

  @Override
  public void displayCurrentForecast(CurrentObservation currentObservation) {
    Log.i(TAG, "displayCurrentForecast: " + currentObservation.getWeather());
  }

  /**
   * This method initializes the RecyclerView as well as the RV adapter and list, if null.
   * It also sets LinearLayoutManager and adapter to RV.
   * @see DailyForecastAdapter
   */
  private void initRecyclerView(){
    if (forecasts == null) forecasts = new ArrayList<>();
    if (forecastAdapter == null) forecastAdapter = new DailyForecastAdapter(forecasts);
    RecyclerView forecastRv = (RecyclerView)findViewById(R.id.forecast_rv);
    forecastRv.setLayoutManager(new LinearLayoutManager(this));
    forecastRv.setAdapter(forecastAdapter);
  }

  /**
   * This method updates the RecyclerView the new list.
   *
   * @param forecastList
   * @see RecyclerView.Adapter#notifyDataSetChanged()
     */
  private void updateList(List<List<Forecast>> forecastList){
    forecasts.clear();
    forecasts.addAll(forecastList);
    if (forecastAdapter != null) forecastAdapter.notifyDataSetChanged();
  }

}
