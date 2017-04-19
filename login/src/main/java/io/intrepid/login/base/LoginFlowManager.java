package io.intrepid.login.base;

import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class LoginFlowManager<T, V extends LoginView> {

    @NonNull
    protected LoginService<T> loginService;
    @NonNull
    protected V loginView;
    protected LoginFlowCallbacks<T> loginFlowCallbacks;
    protected boolean loggingIn = false;
    protected Disposable loginButtonDisposable;
    protected final Scheduler uiScheduler = AndroidSchedulers.mainThread();

    protected LoginFlowManager(Builder<T,V, ? extends Builder> builder) {
        this.loginService = builder.loginService;
        this.loginView = builder.loginView;
        this.loginFlowCallbacks = builder.loginFlowCallbacks;

        if (builder.loginButtonObservable != null) {
            setupLoginButtonWatching(builder.loginButtonObservable);
        }
    }

    protected abstract void setupLoginButtonWatching(Observable<Object> loginButtonObservable);

    public abstract static class Builder<T, V extends LoginView, B extends Builder<T, V, B>> {

        V loginView;
        LoginService<T> loginService;
        LoginFlowCallbacks<T> loginFlowCallbacks;
        Observable<Object> loginButtonObservable;

        public abstract B getBuilder();

        public B setLoginService(LoginService<T> service) {
            this.loginService = service;
            return getBuilder();
        }

        public B setLoginView(V loginView) {
            this.loginView = loginView;
            return getBuilder();
        }

        public B setLoginFlowCallbacks(LoginFlowCallbacks<T> loginFlowCallbacks) {
            this.loginFlowCallbacks = loginFlowCallbacks;
            return getBuilder();
        }

        public B setLoginButtonObservable(Observable<Object> loginButtonObservable) {
            this.loginButtonObservable = loginButtonObservable;
            return getBuilder();
        }
    }
}
