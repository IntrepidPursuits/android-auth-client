package io.intrepid.auth.base;

public interface LoginFlowCallbacks<T> {

    void onLoginSuccess(T response);

    void onLoginError(Throwable throwable);
}
