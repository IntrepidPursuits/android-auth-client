package io.intrepid.login.base;

public interface LoginFlowCallbacks<T> {

    void onLoginSuccess(T response);

    void onLoginError(Throwable throwable);
}
