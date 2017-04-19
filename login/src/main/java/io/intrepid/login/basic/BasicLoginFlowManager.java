package io.intrepid.login.basic;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.intrepid.login.base.LoginFlowManager;
import io.intrepid.login.validation.ValidationRule;
import io.reactivex.Observable;

/**
 * Login Flow Manager class for a basic username/password login flow.
 * @param <T> Type of response expected from the login observable
 * @param <V> BasicLoginView that is associated with this login flow
 */
public final class BasicLoginFlowManager<T, V extends BasicLoginView> extends LoginFlowManager<T, V> {

    private String username;
    private String password;
    @Nullable
    private final ValidationRule usernameValidationRule;
    @Nullable
    private final ValidationRule passwordValidationRule;

    private BasicLoginFlowManager(Builder<T, V> builder) {
        super(builder);
        this.usernameValidationRule = builder.usernameValidationRule;
        this.passwordValidationRule = builder.passwordValidationRule;

        setupTextWatching(builder.usernameFieldObservable, builder.passwordFieldObservable);
    }

    protected void setupLoginButtonWatching(Observable<Object> loginButtonObservable) {
        loginButtonDisposable = loginButtonObservable
                .observeOn(uiScheduler)
                .filter(e -> !loggingIn)
                .doOnNext(v -> loggingIn = true)
                .observeOn(uiScheduler)
                .flatMap(click -> loginService.getLoginObservable())
                .subscribe(
                        response -> {
                            loggingIn = false;
                            loginFlowCallbacks.onLoginSuccess(response);
                            unsubscribeLoginButtonWatching();
                        },
                        throwable -> {
                            loggingIn = false;
                            loginFlowCallbacks.onLoginError(throwable);
                            setupLoginButtonWatching(loginButtonObservable);
                        }
                );
    }

    private void unsubscribeLoginButtonWatching() {
        if (loginButtonDisposable != null) {
            loginButtonDisposable.dispose();
        }
    }

    private void setupTextWatching(Observable<TextViewTextChangeEvent> usernameFieldObservable,
                                   Observable<TextViewTextChangeEvent> passwordFieldObservable) {
        Observable<Boolean> usernameValidationObservable = Observable.just(true);
        Observable<Boolean> passwordValidationObservable = Observable.just(true);

        if (usernameFieldObservable != null) {
             usernameValidationObservable = usernameFieldObservable
                    .doOnNext(e -> {
                        username = e.text().toString();
                        loginService.setUsername(username);
                    })
                    .map(e -> usernameValidationRule == null || usernameValidationRule.validate(username))
                    .observeOn(uiScheduler);
        }

        if (passwordFieldObservable != null) {
             passwordValidationObservable = passwordFieldObservable
                    .doOnNext(e -> {
                        password = e.text().toString();
                        loginService.setPassword(password);
                    })
                    .map(e -> passwordValidationRule == null || passwordValidationRule.validate(password))
                    .observeOn(uiScheduler);
        }

        Observable.combineLatest(usernameValidationObservable, passwordValidationObservable,
                               (usernameValid, passwordValid) -> usernameValid && passwordValid)
                .observeOn(uiScheduler)
                .subscribe(
                        valid -> {
                            if (valid) {
                                loginView.enableLoginButton();
                            } else {
                                loginView.disableLoginButton();
                            }
                        }
                );
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder<T, V extends BasicLoginView> extends LoginFlowManager.Builder<T,V, Builder<T,V>> {
        private ValidationRule usernameValidationRule;
        private ValidationRule passwordValidationRule;
        private Observable<TextViewTextChangeEvent> usernameFieldObservable;
        private Observable<TextViewTextChangeEvent> passwordFieldObservable;

        public BasicLoginFlowManager<T, V> build() {
            return new BasicLoginFlowManager<>(this);
        }

        public Builder<T, V> setUsernameObservable(@NonNull Observable<TextViewTextChangeEvent> usernameFieldObservable,
                                                @Nullable ValidationRule usernameValidationRule) {
            this.usernameFieldObservable = usernameFieldObservable;
            this.usernameValidationRule = usernameValidationRule;
            return this;
        }

        public Builder<T, V> setPasswordObservable(@NonNull Observable<TextViewTextChangeEvent> passwordFieldObservable,
                                                @Nullable ValidationRule passwordValidationRule) {
            this.passwordFieldObservable = passwordFieldObservable;
            this.passwordValidationRule = passwordValidationRule;
            return this;
        }

        @Override
        public Builder<T, V> getBuilder() {
            return this;
        }
    }
}
