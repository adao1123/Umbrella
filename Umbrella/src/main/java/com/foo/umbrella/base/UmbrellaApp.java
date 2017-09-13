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
    /**
     * This method stores the MainPresenter at the application level.
     * Therefore it can live outside of the activity lifecycle.
     * @return MainPresenter
     * @see MainPresenter
     */
    public MainContract.Presenter getMainPresenter(){
        if (mMainPresenter == null){
            mMainPresenter = new MainPresenter();
        }
        return mMainPresenter;
    }

    /**
     * This method will release the MainPresenter from memory.
     * Used to prevent memory leaks.
     */
    public void releaseMainPresenter(){
        mMainPresenter = null;
    }
}
