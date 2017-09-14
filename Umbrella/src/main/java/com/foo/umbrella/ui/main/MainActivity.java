package com.foo.umbrella.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.adapters.DailyForecastAdapter;
import com.foo.umbrella.base.UmbrellaApp;
import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;
import com.foo.umbrella.ui.setting.SettingsActivity;

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
    Log.i(TAG, "onStart: ");
    mPresenter.bindView(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.start(getLastZipCode());
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
    initToolbar(cityTitle);
  }

  @Override
  public void displayCurrentForecast(CurrentObservation currentObservation) {
    Log.i(TAG, "displayCurrentForecast: " + currentObservation.getWeather());
    setCurrentViews(currentObservation);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case R.id.action_setting:
        startActivity(new Intent(this,SettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /**
   * This method initializes the RecyclerView as well as the RV adapter and list, if null.
   * It also sets LinearLayoutManager and adapter to RV.
   * @see DailyForecastAdapter
   */
  private void initRecyclerView(){
    if (forecasts == null) forecasts = new ArrayList<>();
    if (forecastAdapter == null) forecastAdapter = new DailyForecastAdapter(forecasts, isFahrenheit());
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
    if (forecastAdapter != null) {
      forecastAdapter.notifyDataSetChanged();
      forecastAdapter.setIsFahrenheit(isFahrenheit());
    }
  }

  private void initToolbar(String title){
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(title);
    toolbar.inflateMenu(R.menu.toolbar_menu);
    DrawableCompat.setTint(toolbar.getMenu().findItem(R.id.action_setting)
            .getIcon(),ContextCompat.getColor(this,R.color.content_background));
  }

  private boolean isFahrenheit(){
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPref.getString("unit","Fahrenheit").equals("Fahrenheit");
  }

  /**
   * This method gets saved zip code from Shared Preferences
   * @return String zipCode
     */
  private String getLastZipCode(){
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPref.getString("zip","95035");
  }

  private void setCurrentViews(CurrentObservation currentObservation){
    double temperature = (isFahrenheit()) ? currentObservation.getTemp_f() : currentObservation.getTemp_c();
    int color = (currentObservation.getTemp_f() >= 60) ? R.color.weather_warm : R.color.weather_cool;
    ((TextView)findViewById(R.id.current_temp_tv)).setText(String.valueOf(Math.round(temperature))+(char)0x00B0);
    ((TextView)findViewById(R.id.current_condition_tv)).setText(currentObservation.getWeather());
    findViewById(R.id.toolbar).setBackgroundColor(ContextCompat.getColor(this,color));
    findViewById(R.id.current_layout).setBackgroundColor(ContextCompat.getColor(this,color));
  }

}
