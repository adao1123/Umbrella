package com.foo.umbrella.ui.main;

import com.foo.umbrella.data.source.WeatherRepository;


/**
 * Created by adao1 on 9/13/2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private final WeatherRepository mWeatherRepository;
    private MainContract.View mMainView;

    public MainPresenter() {
        mWeatherRepository = WeatherRepository.getINSTANCE();
    }

    @Override
    public void bindView(MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void unbind() {
        mMainView = null;
    }

    @Override
    public void start() {
        initialLoad();
    }

    private void initialLoad(){
    }



}
