package io.intrepid.login.base;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class LoginFlowManager<T> {

    protected final CompositeDisposable disposables = new CompositeDisposable();
    protected final Scheduler ioScheduler = Schedulers.io();
    protected final Scheduler uiScheduler = AndroidSchedulers.mainThread();

    @NonNull
    protected LoginService<T> loginService;
    @NonNull
    protected LoginView loginView;
    protected LoginFlowCallbacks<T> loginFlowCallbacks;
    protected boolean loggingIn = false;

    protected LoginFlowManager(Builder<T, ? extends Builder> builder) {
        this.loginService = builder.loginService;
        this.loginView = builder.loginView;
        this.loginFlowCallbacks = builder.loginFlowCallbacks;

        if (builder.loginButtonObservable != null) {
            setupLoginButtonWatching(builder.loginButtonObservable);
        }
    }

    private void setupLoginButtonWatching(Observable<Object> loginButtonObservable) {
        loginButtonObservable
                .observeOn(uiScheduler)
                .filter(e -> !loggingIn)
                .doOnNext(v -> loggingIn = true)
                .observeOn(uiScheduler)
                .flatMap(click -> loginService.getLoginObservable())
                .subscribe(
                        response -> {
                            loggingIn = false;
                            loginFlowCallbacks.onLoginSuccess(response);
                        },
                        throwable -> {
                            loggingIn = false;
                            loginFlowCallbacks.onLoginError(throwable);
                        }
                );
    }

    public abstract static class Builder<T, B extends Builder<T, B>> {

        LoginView loginView;
        LoginService<T> loginService;
        LoginFlowCallbacks<T> loginFlowCallbacks;
        Observable<Object> loginButtonObservable;

        public abstract B getBuilder();

        public B setLoginService(LoginService<T> service) {
            this.loginService = service;
            return getBuilder();
        }

        public B setLoginView(LoginView loginView) {
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
