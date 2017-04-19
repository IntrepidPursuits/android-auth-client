package io.intrepid.login.basic;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.intrepid.login.base.LoginView;
import io.reactivex.Observable;

public interface BasicLoginView extends LoginView {

    Observable<TextViewTextChangeEvent> getUsernameTextFieldObservable();

    Observable<TextViewTextChangeEvent> getPasswordTextFieldObservable();

    void disableLoginButton();

    void enableLoginButton();
}
