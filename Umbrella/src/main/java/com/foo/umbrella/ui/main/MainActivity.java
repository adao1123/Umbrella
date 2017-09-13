package com.foo.umbrella.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.foo.umbrella.R;
import com.foo.umbrella.base.UmbrellaApp;

public class MainActivity extends AppCompatActivity implements MainContract.View{
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
}
