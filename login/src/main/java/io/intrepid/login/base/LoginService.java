package io.intrepid.login.base;

import android.support.annotation.Nullable;

import io.reactivex.Observable;

public abstract class LoginService<T> {

    @Nullable
    protected String username;
    @Nullable
    protected String password;

    public abstract Observable<T> getLoginObservable();

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }
}
