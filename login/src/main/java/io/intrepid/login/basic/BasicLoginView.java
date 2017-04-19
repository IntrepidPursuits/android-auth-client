package io.intrepid.login.basic;

import io.intrepid.login.base.LoginView;

public interface BasicLoginView extends LoginView {

    void disableLoginButton();

    void enableLoginButton();
}
