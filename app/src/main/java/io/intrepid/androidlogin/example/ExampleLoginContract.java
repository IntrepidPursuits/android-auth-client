package io.intrepid.androidlogin.example;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.intrepid.login.base.LoginView;
import io.intrepid.login.basic.BasicLoginView;
import io.reactivex.Observable;

interface ExampleLoginContract {
    interface View extends BasicLoginView {

        Observable<TextViewTextChangeEvent> getUsernameTextFieldObservable();

        Observable<TextViewTextChangeEvent> getPasswordTextFieldObservable();

        Observable<Object> getLoginButtonObservable();

        void showToast(String msg);
    }

    interface Presenter {
    }
}
