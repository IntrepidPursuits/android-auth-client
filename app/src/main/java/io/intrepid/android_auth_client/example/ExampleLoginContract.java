package io.intrepid.android_auth_client.example;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.intrepid.auth.base.LoginView;
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
