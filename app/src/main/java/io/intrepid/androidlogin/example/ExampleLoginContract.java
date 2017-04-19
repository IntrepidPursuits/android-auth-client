package io.intrepid.androidlogin.example;

import io.intrepid.login.basic.BasicLoginView;

@SuppressWarnings("unused")
interface ExampleLoginContract {
    interface View extends BasicLoginView {
        void showToast(String msg);
    }

    interface Presenter {
    }
}
