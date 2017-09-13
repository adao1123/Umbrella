package com.foo.umbrella.ui.main;

import com.foo.umbrella.base.BasePresenter;
import com.foo.umbrella.base.BaseView;


/**
 * Created by adao1 on 9/13/2017.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {
    }

    public interface Presenter extends BasePresenter {
        void bindView(MainContract.View view);
        void unbind();
    }
}
