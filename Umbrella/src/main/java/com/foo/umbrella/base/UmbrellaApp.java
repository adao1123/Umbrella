package com.foo.umbrella.base;

import android.app.Application;

import com.foo.umbrella.ui.main.MainContract;
import com.foo.umbrella.ui.main.MainPresenter;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class UmbrellaApp extends Application {

    private MainContract.Presenter mMainPresenter;

  @Override
  public void onCreate() {
    super.onCreate();
    AndroidThreeTen.init(this);
  }
    public MainContract.Presenter getMainPresenter(){
        if (mMainPresenter == null){
            mMainPresenter = new MainPresenter();
        }
        return mMainPresenter;
    }

    public void releaseMainPresenter(){
        mMainPresenter = null;
    }
}
