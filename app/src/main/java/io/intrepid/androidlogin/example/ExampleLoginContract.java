package io.intrepid.androidlogin.example;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.intrepid.login.base.LoginView;
import io.reactivex.Observable;

public interface ExampleLoginContract {

    interface View extends LoginView {

        Observable<TextViewTextChangeEvent> getUsernameTextFieldObservable();

        Observable<TextViewTextChangeEvent> getPasswordTextFieldObservable();

        Observable<Object> getLoginButtonObservable();

        void showToast(String msg);
    }

    interface Presenter {
    }
}
