package com.foo.umbrella.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.umbrella.R;
import com.foo.umbrella.adapters.DailyForecastAdapter;
import com.foo.umbrella.base.UmbrellaApp;
import com.foo.umbrella.data.models.CurrentOrigin.CurrentObservation;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;
import com.foo.umbrella.ui.setting.SettingsActivity;
import com.foo.umbrella.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View{

  private static final int COOL_WARM_LINE = 60;
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

  /**
   * Presenter.start will be called in onStart, when the app becomes the foreground app.
   */
  @Override
  protected void onStart() {
    super.onStart();
    mPresenter.bindView(this);
    if (hasNetwork()) mPresenter.start(getLastZipCode());
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
  public void displayZipCodeDialog() {
    showZipcodeDialog();
  }

  @Override
  public void displayWeather(List<List<Forecast>> forecasts) {
    updateList(forecasts);
  }

  @Override
  public void displayCityTitle(String cityTitle) {
    initToolbar(cityTitle);
  }

  @Override
  public void displayCurrentForecast(CurrentObservation currentObservation) {
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

  /**
   * This method configures toolbar.
   * @param title
     */
  private void initToolbar(String title){
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(title);
    toolbar.inflateMenu(R.menu.toolbar_menu);
    DrawableCompat.setTint(toolbar.getMenu().findItem(R.id.action_setting)
            .getIcon(),ContextCompat.getColor(this,R.color.content_background));
  }

  /**
   * This method creates and displays ZipCode Dialog.
   */
  private void showZipcodeDialog(){
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_zipcode, null);
    EditText zipCodeET = (EditText)dialogView.findViewById(R.id.dialog_et);
    dialogBuilder.setView(dialogView);
    Dialog dialog = dialogBuilder.create();
    dialogView.findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveZipPreferences(zipCodeET.getText().toString());
        dialog.dismiss();
      }
    });
    dialog.show();
  }

  /**
   * This method will check the state of preferred unit from SharedPreferences.
   * @return boolean isFahrenheit
     */
  private boolean isFahrenheit(){
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPref.getString(Constants.UNIT_KEY, Constants.FAHRENHEIT).equals(Constants.FAHRENHEIT);
  }

  /**
   * This method gets saved zip code from Shared Preferences
   * @return String zipCode
     */
  private String getLastZipCode(){
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPref.getString(Constants.ZIP_KEY, null);
  }

  /**
   * This method stores zip code into shared preferences.
   * @param zipCode
     */
  private void saveZipPreferences(String zipCode){
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPref.edit().putString(Constants.ZIP_KEY,zipCode).apply();
    mPresenter.start(zipCode);
  }

  /**
   * This method fill data into current forecast views.
   * @param currentObservation
     */
  private void setCurrentViews(CurrentObservation currentObservation){
    double temperature = (isFahrenheit()) ? currentObservation.getTemp_f() : currentObservation.getTemp_c();
    int color = (currentObservation.getTemp_f() >= COOL_WARM_LINE) ? R.color.weather_warm : R.color.weather_cool;
    ((TextView)findViewById(R.id.current_temp_tv)).setText(String.valueOf(Math.round(temperature))+(char)0x00B0);
    ((TextView)findViewById(R.id.current_condition_tv)).setText(currentObservation.getWeather());
    findViewById(R.id.toolbar).setBackgroundColor(ContextCompat.getColor(this,color));
    findViewById(R.id.current_layout).setBackgroundColor(ContextCompat.getColor(this,color));
  }

  /**
   * This method checks if there is a network connection.
   * @return boolean
   * @see ConnectivityManager
   * @see ConnectivityManager#getActiveNetwork()
   */
  private boolean hasNetwork(){
    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()) return true;
    Toast.makeText(this,"No Network Connected", Toast.LENGTH_SHORT).show();
    return false;
  }

}
